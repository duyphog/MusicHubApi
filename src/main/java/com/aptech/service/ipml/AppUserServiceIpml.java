package com.aptech.service.ipml;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.aptech.constant.BeanIdConstant;
import com.aptech.domain.AppUserDomain;
import com.aptech.dto.AppUserDto;
import com.aptech.dto.UserInfoDto;
import com.aptech.dto.UserRegister;
import com.aptech.entity.AppUser;
import com.aptech.entity.UserInfo;
import com.aptech.entity.VerificationToken;
import com.aptech.handle.exception.EmailExistException;
import com.aptech.handle.exception.UsernameExistException;
import com.aptech.repository.AppUserRepository;
import com.aptech.repository.UserInfoRepository;
import com.aptech.repository.VerificationTokenRepository;
import com.aptech.service.IAppUserService;
import com.aptech.service.IAppMailService;
import com.aptech.util.AppUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

@Service
@Qualifier(BeanIdConstant.USER_DETAIL_SERVICE)
public class AppUserServiceIpml implements IAppUserService, UserDetailsService {

	private final Logger logger = LoggerFactory.getLogger(AppUserServiceIpml.class);

	private AppUserRepository appUserRepository;

	private UserInfoRepository userInfoRepository;

	private VerificationTokenRepository verificationTokenRepository;

	private IAppMailService appMailService;

	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	public AppUserServiceIpml(AppUserRepository appUserRepository, UserInfoRepository userInfoRepository,
			VerificationTokenRepository verificationTokenRepository, IAppMailService appMailService,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.appUserRepository = appUserRepository;
		this.userInfoRepository = userInfoRepository;
		this.verificationTokenRepository = verificationTokenRepository;
		this.appMailService = appMailService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	public AppUserDto register(UserRegister userRegister) throws EmailExistException, UsernameExistException {
		try {
			AppUser userByEmail = appUserRepository.findByEmail(userRegister.getEmail());
			if (userByEmail != null)
				throw new EmailExistException("Email is exist: " + userRegister.getEmail());
			// TODO: Check email confirm, return message or resend mail

			AppUser userByUsername = appUserRepository.findByUsername(userRegister.getUsername());
			if (userByUsername != null)
				throw new UsernameExistException("Username is exist: " + userRegister.getUsername());

			ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);

			AppUser userNew = mapper.convertValue(userRegister, AppUser.class);

			// TODO: Set roles, authorities
			// userNew.setAppRoles(appRoles);
			// userNew.setAuthorities(new Set<AppAuthority>());

			userNew.setEnabled(true);
			userNew.setAccountNonLocked(true);

			// endcode password
			userNew.setPassword(bCryptPasswordEncoder.encode(userRegister.getPassword()));

			// Save user
			appUserRepository.save(userNew);

			// Send mail verify
			appMailService.sendMailVerify(userNew);

			AppUserDto userDto = mapper.convertValue(userNew, AppUserDto.class);

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

		if (vToken != null) {
			if (vToken.getVerifyDate() != null)
				return false;

			vToken.setVerify(true);
			vToken.setVerifyDate(AppUtil.getNow());

			vToken.getAppUser().setEnabled(true);

			verificationTokenRepository.save(vToken);

			return true;
		} else {
			logger.warn("Token is not exist: " + token.toString());

			return false;
		}
	}

	@SneakyThrows
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser appUser = appUserRepository.findByUsername(username);

		appUserRepository.findByUsername(username);

		if (appUser == null) {
			throw new UsernameNotFoundException("User not found by username: " + username);
		}

		AppUserDomain appUserDomain = new AppUserDomain(appUser);

		return appUserDomain;
	}

	@Override
	public UserInfoDto getProfile(UUID userId) {

		UserInfoDto userInfoDto = new UserInfoDto();

		try {
			AppUser user = appUserRepository.findById(userId).orElse(null);
			AppUser users = appUserRepository.findByUsername("phongnd");
					
			if(user == null)
				return null;
			
			if (user.getUserInfo() != null) {
				ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
						false);

				userInfoDto = mapper.convertValue(user, UserInfoDto.class);
				
			} else {
				userInfoDto.setUserId(user.getId().toString());
				userInfoDto.setEmail(user.getEmail());
				userInfoDto.setUsername(user.getUsername());
			}

			return userInfoDto;

		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			return null;
		}
	}
}
