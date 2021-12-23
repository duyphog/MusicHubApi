package com.aptech.service.ipml;

import java.io.IOException;
import java.util.UUID;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import com.aptech.dto.UserDto;
import com.aptech.dto.UserRegister;
import com.aptech.entity.AppUser;
import com.aptech.entity.VerificationToken;
import com.aptech.handle.exception.EmailExistException;
import com.aptech.handle.exception.UsernameExistException;
import com.aptech.repository.AppUserRepository;
import com.aptech.repository.VerificationTokenRepository;
import com.aptech.service.IAppUserService;
import com.aptech.service.IEmailSenderService;
import com.aptech.util.AppUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AppUserService implements IAppUserService {

	private static final Logger logger = LoggerFactory.getLogger(AppUserService.class);

	@Autowired
	private AppUserRepository appRepository;
	
	@Autowired
	private VerificationTokenRepository verificationTokenRepository;

	@Autowired
	private IEmailSenderService emailSenderService;

//	@Autowired
//	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public UserDto register(UserRegister userRegister) throws EmailExistException, UsernameExistException {
		try {
			AppUser userByEmail = appRepository.findByEmail(userRegister.getEmail());
			if (userByEmail != null)
				throw new EmailExistException("Email is exist: " + userRegister.getEmail());

			AppUser userByUsername = appRepository.findByUsername(userRegister.getUsername());
			if (userByUsername != null)
				throw new UsernameExistException("Username is exist: " + userRegister.getUsername());

			ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);

			AppUser userNew = mapper.convertValue(userRegister, AppUser.class);
			userNew.setEnabled(false);
			userNew.setStatus(true);

//			userNew.setPassword(bCryptPasswordEncoder.encode(userRegister.getPassword()));
			appRepository.save(userNew);

			SendEmailConfirmAsync(userNew);

			UserDto userDto = mapper.convertValue(userNew, UserDto.class);

			return userDto;

		} catch (EmailExistException | UsernameExistException e) {
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}

	@Override
	public boolean verifyEmail(UUID token) {
		
		VerificationToken vToken = verificationTokenRepository.findByToken(token);
		
		if(vToken != null) {
			vToken.setVerify(true);
			vToken.setVerifyDate(AppUtil.getNow());
			
			vToken.getAppUser().setEnabled(true);
			
			verificationTokenRepository.save(vToken);
			
			return true;
		}
		else {
			logger.warn("Token is not exist: " +  token.toString());
			
			return false;
		}
	}

	@Async
	public void SendEmailConfirmAsync(AppUser appUser) {
		
		VerificationToken vToken = new VerificationToken(); 
		vToken.setToken(UUID.randomUUID());
		vToken.setAppUser(appUser);
		vToken.setDateNew(AppUtil.getNow());
		vToken.setVerify(false);
		vToken.setSend(false);
		
		verificationTokenRepository.save(vToken);
		
		try {
			emailSenderService.sendVerifyEmailRegister(appUser.getEmail(), vToken.getToken().toString());
			vToken.setSend(true);
			vToken.setLastTime(AppUtil.getNow());
			
		} catch (MessagingException | IOException e) {
			vToken.setSend(false);
			logger.error(e.getMessage());
		}
		
		verificationTokenRepository.save(vToken);
	}

}
