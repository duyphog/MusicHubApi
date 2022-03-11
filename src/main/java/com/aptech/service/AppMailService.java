package com.aptech.service;

import com.aptech.entity.AppUser;

public interface AppMailService {

	void sendMailVerify(AppUser appUser);
	
	void sendResetPassword(String email, String newPassword);
}
