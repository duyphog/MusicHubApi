package com.aptech.controller;


import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import com.aptech.service.ipml.AppUserService;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private AppUserService appUserService;

	@PostMapping("/register")
	public ResponseEntity<HttpResponse> register(@Valid @RequestBody UserRegister userRegister)
			throws EmailExistException, UsernameExistException {
		UserDto user = appUserService.register(userRegister);

		return ResponseEntity.ok(new HttpResponseSuccess<UserDto>(user));
	}
}
