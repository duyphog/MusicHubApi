package com.aptech.service;

import com.aptech.entity.AppUser;

public interface IAppMailService {

	void sendMailVerify(AppUser appUser);
	
	void sendResetPassword(String email, String newPassword);
}
