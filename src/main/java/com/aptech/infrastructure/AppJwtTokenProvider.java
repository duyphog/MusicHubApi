package com.aptech.infrastructure;

import com.aptech.constant.SecurityConstant;
import com.aptech.domain.AppUserDomain;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@Component
public class AppJwtTokenProvider {

	@Value("${jwt.secret}")
	private String secret;

	public String generateJwtToken(AppUserDomain appUserDetails) {
		
		String[] claims = getClaimsFromUser(appUserDetails);

		return JWT.create().withIssuer(SecurityConstant.COMPANY).withAudience(SecurityConstant.APPLICATION_NAME)
				.withIssuedAt(new Date()).withSubject(appUserDetails.getUsername())
				.withArrayClaim(SecurityConstant.AUTHORITIES, claims)
				.withExpiresAt(new Date(System.currentTimeMillis() + SecurityConstant.EXPIRATION_TIME))
				.sign(Algorithm.HMAC512(secret.getBytes()));
	}

	private String[] getClaimsFromUser(AppUserDomain appUserDetails) {
		List<String> authorities = new ArrayList<String>();
		for (GrantedAuthority grantedAuthority : appUserDetails.getAuthorities()) {
			authorities.add(grantedAuthority.getAuthority());
		}
		return authorities.toArray(new String[0]);
	}

	private JWTVerifier getJwtVerifier() {
		JWTVerifier verifier;
		try {
			Algorithm algorithm = Algorithm.HMAC512(secret);
			verifier = JWT.require(algorithm).withIssuer(SecurityConstant.COMPANY).build();
		} catch (JWTVerificationException ex) {
			throw new JWTVerificationException(SecurityConstant.TOKEN_CANNOT_BE_VERIFIED);
		}
		return verifier;
	}

	public boolean isTokenValid(String username, String token) {
		JWTVerifier verifier = getJwtVerifier();

		Date expirationDate = verifier.verify(token).getExpiresAt();
		boolean isTokenExpired = expirationDate.before(new Date());

		return !username.isEmpty() && !isTokenExpired;
	}

	public String getSubject(String token) {
		JWTVerifier verifier = getJwtVerifier();
		return verifier.verify(token).getSubject();
	}

	public List<GrantedAuthority> getAuthoritiesFromToken(String token) {
		JWTVerifier verifier = getJwtVerifier();
		String[] claims = verifier.verify(token).getClaim(SecurityConstant.AUTHORITIES).asArray(String.class);
		return stream(claims).map(SimpleGrantedAuthority::new).collect(Collectors.toList());
	}

	// tell Spring to give us the Authentication
	public Authentication getAuthentication(String username, List<GrantedAuthority> authorities,
			HttpServletRequest request) {
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				username, null, authorities);
		usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		return usernamePasswordAuthenticationToken;
	}
}
