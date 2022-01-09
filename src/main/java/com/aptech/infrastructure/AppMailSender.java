package com.aptech.infrastructure;

import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class AppMailSender {

	@Autowired
	private JavaMailSender javaMailSender;

	public void sendMimeMessage(String mailFrom, String mailTo, String text, String subject, boolean html)
			throws MessagingException {

		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
				StandardCharsets.UTF_8.name());

		helper.setTo(mailTo);
		helper.setText(text, html);
		helper.setSubject(subject);
		helper.setFrom(mailFrom);

		javaMailSender.send(message);
	}
}
