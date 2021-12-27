 package com.aptech.service;

import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.aptech.dto.AppUserDto;
import com.aptech.dto.UserInfoDto;
import com.aptech.dto.UserRegister;
import com.aptech.handle.exception.EmailExistException;
import com.aptech.handle.exception.UsernameExistException;

public interface IAppUserService {
	AppUserDto register(UserRegister userRegister) throws EmailExistException, UsernameExistException;

	boolean verifyEmail(UUID token);
	
	UserInfoDto getProfile(UUID userId);
	
	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
