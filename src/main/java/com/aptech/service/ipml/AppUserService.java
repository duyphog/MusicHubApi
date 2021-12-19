package com.aptech.service.ipml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.aptech.dto.UserDto;
import com.aptech.dto.UserRegister;
import com.aptech.entity.AppUser;
import com.aptech.handle.exception.EmailExistException;
import com.aptech.handle.exception.UsernameExistException;
import com.aptech.repository.AppUserRepository;
import com.aptech.service.IAppUserService;
import com.aptech.service.IEmailSenderService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class AppUserService implements IAppUserService {

	private static final Logger logger = LoggerFactory.getLogger(AppUserService.class);

	@Autowired
	private AppUserRepository appRepository;

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

//			userNew.setPassword(bCryptPasswordEncoder.encode(userRegister.getPassword()));
			appRepository.save(userNew);

			emailSenderService.sendVerifyEmailRegister(userNew.getEmail(), "https://www.google.com/");

			UserDto userDto = mapper.convertValue(userNew, UserDto.class);

			return userDto;

		} catch (EmailExistException | UsernameExistException e) {
			throw e;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}

}
