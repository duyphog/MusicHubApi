package com.aptech.service;

import java.util.UUID;

import com.aptech.dto.UserDto;
import com.aptech.dto.UserLogin;
import com.aptech.dto.UserRegister;
import com.aptech.handle.exception.EmailExistException;
import com.aptech.handle.exception.UsernameExistException;

public interface IAppUserService {
	UserDto register(UserRegister userRegister) throws EmailExistException, UsernameExistException;

	boolean verifyEmail(UUID token);
	
	UserDto login(UserLogin userLogin);
}
