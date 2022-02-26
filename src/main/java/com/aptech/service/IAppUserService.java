 package com.aptech.service;

import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import com.aptech.domain.AppBaseResult;
import com.aptech.domain.AppServiceResult;
import com.aptech.dto.user.ChangePassword;
import com.aptech.dto.user.UserRegister;
import com.aptech.dto.user.UserWhiteList;
import com.aptech.dto.userinfo.UserInfoDtoReq;
import com.aptech.dto.userinfo.UserInfoDtoRes;
import com.aptech.handle.exception.NotAnImageFileException;

public interface IAppUserService {
	AppBaseResult register(UserRegister userRegister);

	AppBaseResult verifyEmail(UUID token);
	
	AppServiceResult<UserInfoDtoRes> getProfile(Long userId);
	
	AppBaseResult saveProfile(UserInfoDtoReq userInfo);
	
	AppBaseResult changePassword(ChangePassword changePassword);
	
	AppBaseResult resetPassword(String email);
	
	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
	
	AppServiceResult<String> uploadImage(MultipartFile file) throws NotAnImageFileException;
	
	AppBaseResult updateWhiteList(UserWhiteList dto);
}
