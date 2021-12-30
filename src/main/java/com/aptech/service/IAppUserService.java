 package com.aptech.service;

import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import com.aptech.domain.AppBaseResult;
import com.aptech.domain.AppServiceResult;
import com.aptech.dto.ChangePassword;
import com.aptech.dto.UserInfoDtoReq;
import com.aptech.dto.UserInfoDtoRes;
import com.aptech.dto.UserRegister;

public interface IAppUserService {
	AppBaseResult register(UserRegister userRegister);

	AppBaseResult verifyEmail(UUID token);
	
	AppServiceResult<UserInfoDtoRes> getProfile(Long userId);
	
	AppBaseResult saveProfile(UserInfoDtoReq userInfo);
	
	AppBaseResult changePassword(ChangePassword changePassword);
	
	AppBaseResult resetPassword(String email);
	
	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
	
	AppBaseResult uploadImage(MultipartFile file);
}
