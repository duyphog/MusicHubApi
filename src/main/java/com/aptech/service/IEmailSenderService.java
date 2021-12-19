package com.aptech.service;

import java.io.IOException;

import javax.mail.MessagingException;

public interface IEmailSenderService {

	void sendVerifyEmailRegister(String mailTo, String actionUrl)
			throws MessagingException, IOException;

}
