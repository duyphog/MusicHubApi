package com.aptech.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.aptech.constant.SecurityConstant;
import com.aptech.infrastructure.AppJwtTokenProvider;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

	private AppJwtTokenProvider jwtTokenProvider;

	@Autowired
	public JwtAuthorizationFilter(AppJwtTokenProvider jwtTokenProvider) {
		this.jwtTokenProvider = jwtTokenProvider;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		if (request.getMethod().equalsIgnoreCase(SecurityConstant.OPTIONS_HTTP_METHOD)) {
			response.setStatus(HttpStatus.OK.value());
		} else {
			String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
			if (authorizationHeader == null || !authorizationHeader.startsWith(SecurityConstant.TOKEN_PREFIX)) {
				filterChain.doFilter(request, response);
				return;
			}

			// get token
			String token = authorizationHeader.substring(SecurityConstant.TOKEN_PREFIX.length());

			// get username from Token
			String username = jwtTokenProvider.getSubject(token);

			// validate token
			if (jwtTokenProvider.isTokenValid(username, token)) {

				// get privilege from token
				List<GrantedAuthority> authorities = jwtTokenProvider.getAuthoritiesFromToken(token);

				// set privilege -> SecurityContext
				Authentication authentication = jwtTokenProvider.getAuthentication(username, authorities, request);

				SecurityContextHolder.getContext().setAuthentication(authentication);
			} else {
				SecurityContextHolder.clearContext();
			}
		}

		filterChain.doFilter(request, response);
	}
}
