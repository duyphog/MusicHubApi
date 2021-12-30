 package com.aptech.service;

import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import com.aptech.domain.AppBaseResult;
import com.aptech.domain.AppServiceResult;
import com.aptech.dto.ChangePassword;
import com.aptech.dto.UserInfoDto;
import com.aptech.dto.UserRegister;

public interface IAppUserService {
	AppBaseResult register(UserRegister userRegister);

	AppBaseResult verifyEmail(UUID token);
	
	AppServiceResult<UserInfoDto> getProfile(Long userId);
	
	AppBaseResult saveProfile(UserInfoDto userInfo);
	
	AppBaseResult changePassword(ChangePassword changePassword);
	
	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
	
	AppBaseResult uploadImage(MultipartFile file);
}
