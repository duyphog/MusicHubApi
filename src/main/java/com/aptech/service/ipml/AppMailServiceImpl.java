package com.aptech.service.ipml;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.aptech.constant.EmailSenderConstant;
import com.aptech.entity.AppUser;
import com.aptech.entity.VerificationToken;
import com.aptech.infrastructure.AppMailSender;
import com.aptech.repository.VerificationTokenRepository;
import com.aptech.service.IAppMailService;
import com.aptech.util.AppUtil;

@Service
public class AppMailServiceImpl implements IAppMailService {

	private final Logger logger = LoggerFactory.getLogger(AppMailServiceImpl.class);

	public static final String TEMPLATE_VERIFY_EMAIL_NAME = "VerifyEmail";

	@Value("${spring.mail.username}")
	private String emailFromAddress;

	@Value("${app.config.baseurl.verify.email}")
	private String baseUrlVerifyEmail;

	private SpringTemplateEngine templateEngine;

	private AppMailSender mailSender;

	private VerificationTokenRepository verificationTokenRepository;

	@Autowired
	public AppMailServiceImpl(SpringTemplateEngine templateEngine,
			VerificationTokenRepository verificationTokenRepository, AppMailSender mailSender) {
		this.templateEngine = templateEngine;
		this.verificationTokenRepository = verificationTokenRepository;
		this.mailSender = mailSender;
	}

	@Override
	@Async("threadPoolTaskExecutorForVerifyEmail")
	public void sendMailVerify(AppUser appUser) {
		VerificationToken vToken = new VerificationToken();
		vToken.setToken(UUID.randomUUID());
		vToken.setAppUser(appUser);
		vToken.setDateNew(AppUtil.getNow());
		vToken.setVerify(false);
		vToken.setSend(false);

		verificationTokenRepository.save(vToken);

		boolean isSuccess = sendVerifyEmailRegister(appUser.getEmail(), vToken.getToken().toString());

		vToken.setSend(isSuccess);
		vToken.setLastTime(AppUtil.getNow());

		verificationTokenRepository.save(vToken);
	}

	private boolean sendVerifyEmailRegister(String email, String token) {
		try {
			final Context context = new Context();
			context.setVariable("user_verify_link", baseUrlVerifyEmail + token);
			String html = templateEngine.process(TEMPLATE_VERIFY_EMAIL_NAME, context);

			mailSender.sendMimeMessage(emailFromAddress, email, html, EmailSenderConstant.REGISTER_VETIFY_MAIL_SUBJECT,
					true);

			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return false;
	}
}