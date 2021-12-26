package com.aptech.controller;

import java.net.URI;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aptech.domain.AppUserDetails;
import com.aptech.domain.HttpResponse;
import com.aptech.domain.HttpResponseError;
import com.aptech.domain.HttpResponseSuccess;
import com.aptech.dto.UserDto;
import com.aptech.dto.UserInfoDto;
import com.aptech.dto.UserLogin;
import com.aptech.dto.UserRegister;
import com.aptech.handle.exception.EmailExistException;
import com.aptech.handle.exception.UsernameExistException;
import com.aptech.infrastructure.AppJwtTokenProvider;
import com.aptech.service.IAppUserService;

@RestController
@RequestMapping("/user")
public class UserController {

	private IAppUserService appUserService;
	
	private AuthenticationManager authenticationManager;
	
	private AppJwtTokenProvider appJwtTokenProvider;
	
	@Value("${app.config.url.loginapp}")
	private String urlLoginApp;
	
	
	@Autowired
	public UserController(IAppUserService appUserService, AuthenticationManager authenticationManager, AppJwtTokenProvider appJwtTokenProvider) {
		this.appJwtTokenProvider = appJwtTokenProvider;
		this.authenticationManager = authenticationManager;
		this.appUserService = appUserService;
	}
	
	@PostMapping("/register")
	public ResponseEntity<HttpResponse> register(@Valid @RequestBody UserRegister userRegister)
			throws EmailExistException, UsernameExistException {
		
		UserDto user = appUserService.register(userRegister);
		
		return user != null 
				? ResponseEntity.ok(new HttpResponseSuccess<UserDto>(user))
				: ResponseEntity.ok(new HttpResponseError(null, "Unknow error!"));
	}

	@GetMapping("/verify/{token}")
	public ResponseEntity<Void> verifyEmail(@PathVariable(name = "token", required = true) UUID token) {

		boolean verifyResult = appUserService.verifyEmail(token);
		String url = urlLoginApp + (verifyResult ? "=success" : "=fail");

		return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(url)).build();
	}

	@PostMapping("/login")
	public ResponseEntity<HttpResponse> login(@Valid @RequestBody UserLogin userLogin) {

		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLogin.getUsername(), userLogin.getPassword()));
		
		AppUserDetails appUserDetails = (AppUserDetails) authentication.getPrincipal();
		
		String userToken = appJwtTokenProvider.generateJwtToken(appUserDetails);
		UserDto userDto = new UserDto(appUserDetails.getUsername(), appUserDetails.getEmail(), userToken);
		
		return ResponseEntity.ok(new HttpResponseSuccess<UserDto>(userDto));
	}
	
	@GetMapping("/profiles")
	public ResponseEntity<HttpResponse> getProfiles(@Valid @RequestParam UUID userId) {
		
		UserInfoDto userInfo = appUserService.getProfile(userId);
		
		return userInfo != null 
				? ResponseEntity.ok(new HttpResponseSuccess<UserInfoDto>(userInfo))
				: ResponseEntity.ok(new HttpResponseError(null, "Unknow error!"));
	}
}
