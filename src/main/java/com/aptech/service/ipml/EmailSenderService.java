package com.aptech.service.ipml;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.aptech.constant.EmailSenderConstant;
import com.aptech.service.IEmailSenderService;

@Service
public class EmailSenderService implements IEmailSenderService {

	public static final String TEMPLATE_VERIFY_EMAIL_NAME = "VerifyEmail";

	@Autowired
	private JavaMailSender javaMailSender;

	@Autowired
	private SpringTemplateEngine templateEngine;

	@Value("${spring.mail.username}")
	private String emailFromAddress;
	
	@Value("${app.config.baseurl.verify.email}")
	private String baseUrlVerifyEmail;

	
	//TODO: handle delay
	@Override
	public void sendVerifyEmailRegister(String mailTo, String token) throws MessagingException, IOException {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());

		final Context context = new Context();
		context.setVariable("user_verify_link", baseUrlVerifyEmail + token);

		String html = templateEngine.process(TEMPLATE_VERIFY_EMAIL_NAME, context);

		helper.setTo(mailTo);
		helper.setText(html, true);
		helper.setSubject(EmailSenderConstant.REGISTER_VETIFY_MAIL_SUBJECT);
		helper.setFrom(emailFromAddress);

		javaMailSender.send(message);
	}
}
