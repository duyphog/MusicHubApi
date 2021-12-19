package com.aptech.service;

import com.aptech.dto.UserDto;
import com.aptech.dto.UserRegister;
import com.aptech.handle.exception.EmailExistException;
import com.aptech.handle.exception.UsernameExistException;

public interface IAppUserService {
	UserDto register(UserRegister userRegister) throws EmailExistException, UsernameExistException;
}
