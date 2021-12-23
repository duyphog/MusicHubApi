package com.aptech.controller;

import java.lang.ProcessBuilder.Redirect;
import java.net.URI;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptech.domain.HttpResponse;
import com.aptech.domain.HttpResponseSuccess;
import com.aptech.dto.UserDto;
import com.aptech.dto.UserRegister;
import com.aptech.handle.exception.EmailExistException;
import com.aptech.handle.exception.UsernameExistException;
import com.aptech.service.IAppUserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private IAppUserService appUserService;

	@PostMapping("/register")
	public ResponseEntity<HttpResponse> register(@Valid @RequestBody UserRegister userRegister)
			throws EmailExistException, UsernameExistException {
		UserDto user = appUserService.register(userRegister);

		return ResponseEntity.ok(new HttpResponseSuccess<UserDto>(user));
	}

	@GetMapping("/verify/{token}")
	public ResponseEntity<Void> verifyEmail(@PathVariable(name = "token", required = true) UUID token) {

		boolean verifyResult = appUserService.verifyEmail(token);
		String url = verifyResult ? "https://www.google.com/search?q=success" : "https://www.google.com/search?q=fail";

		return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(url)).build();
	}
}
