package com.aptech.service.ipml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.aptech.constant.AppError;
import com.aptech.constant.BeanIdConstant;
import com.aptech.constant.FileConstant;
import com.aptech.constant.RoleConstant;
import com.aptech.domain.AppBaseResult;
import com.aptech.domain.AppServiceResult;
import com.aptech.domain.AppUserDomain;
import com.aptech.dto.ChangePassword;
import com.aptech.dto.UserInfoDtoReq;
import com.aptech.dto.UserInfoDtoRes;
import com.aptech.dto.UserRegister;
import com.aptech.entity.AppRole;
import com.aptech.entity.AppUser;
import com.aptech.entity.UserInfo;
import com.aptech.entity.VerificationToken;
import com.aptech.repository.AppRoleRepository;
import com.aptech.repository.AppUserRepository;
import com.aptech.repository.VerificationTokenRepository;
import com.aptech.service.IAppUserService;
import com.aptech.service.IAppMailService;
import com.aptech.util.AppUtil;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@Qualifier(BeanIdConstant.USER_DETAIL_SERVICE)
public class AppUserServiceIpml implements IAppUserService, UserDetailsService {

	private final Logger logger = LoggerFactory.getLogger(AppUserServiceIpml.class);

	private AppUserRepository appUserRepository;

	private VerificationTokenRepository verificationTokenRepository;

	private AppRoleRepository appRoleRepository;

	private IAppMailService appMailService;

	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Autowired
	public AppUserServiceIpml(AppUserRepository appUserRepository, AppRoleRepository appRoleRepository,
			VerificationTokenRepository verificationTokenRepository, IAppMailService appMailService,
			BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.appUserRepository = appUserRepository;
		this.verificationTokenRepository = verificationTokenRepository;
		this.appRoleRepository = appRoleRepository;
		this.appMailService = appMailService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@Override
	public AppBaseResult register(UserRegister userRegister) {
		try {
			ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);

			AppUser userByEmail = appUserRepository.findByEmail(userRegister.getEmail());
			if (userByEmail != null) {
				logger.warn("Email is exist: " + userRegister.getEmail() + ", Cannot further process!");
				return new AppBaseResult(false, AppError.Validattion.errorCode(),
						"Email is exist: " + userRegister.getEmail());
			}

			// TODO: Check email confirm, return message or resend mail

			AppUser userByUsername = appUserRepository.findByUsername(userRegister.getUsername());
			if (userByUsername != null) {
				logger.warn("Username is exist: " + userRegister.getUsername() + ", Cannot further process!");
				return new AppBaseResult(false, AppError.Validattion.errorCode(),
						"Username is exist: " + userRegister.getUsername());
			}

			AppUser userNew = mapper.convertValue(userRegister, AppUser.class);

			AppRole defaultRole = appRoleRepository.findByName(RoleConstant.ROLE_MEMBER);
			userNew.setAppRoles(Stream.of(defaultRole).collect(Collectors.toSet()));

			// TODO: Set authorities

			userNew.setUserInfo(new UserInfo());
			// Random image
			userNew.getUserInfo().setAvatarImg(FileConstant.TEMP_PROFILE_IMAGE_BASE_URL + userRegister.getUsername());

			userNew.setEnabled(false);

			userNew.setAccountNonLocked(true);

			// endcode password
			userNew.setPassword(bCryptPasswordEncoder.encode(userRegister.getPassword()));

			// Save user
			appUserRepository.save(userNew);

			// Send mail verify
			appMailService.sendMailVerify(userNew);

			return new AppBaseResult(true, 0, "Success");

		} catch (Exception e) {
			e.printStackTrace();
			return new AppBaseResult(false, AppError.Unknown.errorCode(), AppError.Unknown.errorMessage());
		}
	}

	@Override
	public AppBaseResult verifyEmail(UUID token) {
		VerificationToken vToken = verificationTokenRepository.findByToken(token);

		if (vToken != null) {
			if (vToken.getVerifyDate() != null) {
				logger.warn("Token verified!");
				return new AppBaseResult(false, AppError.Validattion.errorCode(), "Token verified!");
			}

			vToken.setVerify(true);
			vToken.setVerifyDate(AppUtil.getNow());

			vToken.getAppUser().setEnabled(true);

			verificationTokenRepository.save(vToken);

			return new AppBaseResult(true, 0, "Success");
		} else {
			logger.warn("Token is not exist: " + token.toString());
			return new AppBaseResult(false, AppError.Unknown.errorCode(), "Token is not exist!");
		}
	}

	@SneakyThrows
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		AppUser appUser = appUserRepository.findByUsername(username);

		appUserRepository.findByUsername(username);

		if (appUser == null) {
			logger.warn("User not found by username: " + username + ". Cannot further process!");
			throw new UsernameNotFoundException("User not found by username: " + username);
		}

		AppUserDomain appUserDomain = new AppUserDomain(appUser);

		return appUserDomain;
	}

	@Override
	public AppServiceResult<UserInfoDtoRes> getProfile(Long userId) {

		UserInfoDtoRes userInfoDto = new UserInfoDtoRes();
		try {
			AppUser user = appUserRepository.findById(userId).orElse(null);

			if (user == null) {
				logger.warn("AppUser is null, Cannot further process!");
				return new AppServiceResult<UserInfoDtoRes>(false, AppError.Validattion.errorCode(), "User is not exist!",
						null);
			}

			userInfoDto.setUserId(user.getId());
			userInfoDto.setEmail(user.getEmail());
			userInfoDto.setUsername(user.getUsername());

			if (user.getUserInfo() != null) {

				// TODO: Implement mapping
				userInfoDto.setFirstName(user.getUserInfo().getFirstName());
				userInfoDto.setLastName(user.getUserInfo().getLastName());
				userInfoDto.setAvatarImg(user.getUserInfo().getAvatarImg());
				userInfoDto.setStory(user.getUserInfo().getStory());
			}

			return new AppServiceResult<UserInfoDtoRes>(true, 0, "Success", userInfoDto);

		} catch (Exception e) {
			e.printStackTrace();
			return new AppServiceResult<UserInfoDtoRes>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}

	@Override
	public AppBaseResult saveProfile(UserInfoDtoReq userInfo) {
		try {
			String currentUsername = AppUtil.getCurrentUsername();

			if (currentUsername != null) {

				AppUser user = appUserRepository.findByUsername(currentUsername);
				if (userInfo.getUserId() != user.getId()) {
					logger.warn("Not match UserId, Cannot further process!");
					return new AppBaseResult(false, AppError.Validattion.errorCode(), "User is not match id");
				}

				// TODO: Implement mapping
				user.setEmail(userInfo.getEmail());
				user.getUserInfo().setFirstName(userInfo.getFirstName());
				user.getUserInfo().setLastName(userInfo.getLastName());
				user.getUserInfo().setStory(userInfo.getStory());

				user.getUserInfo().setUserEdit(currentUsername);

				// Save user
				appUserRepository.save(user);
				return new AppBaseResult(true, 0, "Success");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new AppBaseResult(false, AppError.Unknown.errorCode(), AppError.Unknown.errorMessage());
	}

	@Override
	public AppBaseResult changePassword(ChangePassword changePassword) {
		try {
			String currentUsername = AppUtil.getCurrentUsername();

			if (!currentUsername.equals(changePassword.getUsername())) {
				logger.warn("Not match UserId, Cannot further process!");
				return new AppBaseResult(false, AppError.Validattion.errorCode(), "Not match UserId");
			}

			if (changePassword.getNewPassword().equals(changePassword.getOldPassword())) {
				logger.warn("New password euqals old password, Cannot further process!");
				return new AppBaseResult(false, AppError.Validattion.errorCode(), "New password euqals old password");
			}

			AppUser user = appUserRepository.findByUsername(currentUsername);

			if (user == null) {
				logger.warn("User is not exist!, Cannot further process!");
				return new AppBaseResult(false, AppError.Validattion.errorCode(), "User is not exist!");
			}

			if (!bCryptPasswordEncoder.matches(changePassword.getOldPassword(), user.getPassword())) {
				logger.warn("Password incorrect, Cannot further process!");
				return new AppBaseResult(false, AppError.Validattion.errorCode(), "Password incorrect!");
			}

			user.setPassword(bCryptPasswordEncoder.encode(changePassword.getNewPassword()));
			user.setUserEdit(currentUsername);

			appUserRepository.save(user);

			return new AppBaseResult(true, AppError.Validattion.errorCode(), "Success");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}

		return new AppBaseResult(false, AppError.Unknown.errorCode(), AppError.Unknown.errorMessage());
	}

	@Override
	public AppBaseResult uploadImage(MultipartFile file) {
		try {
			if (file != null) {
				if (!Arrays.asList(MimeTypeUtils.IMAGE_JPEG_VALUE, MimeTypeUtils.IMAGE_GIF_VALUE,
						MimeTypeUtils.IMAGE_PNG_VALUE).contains(file.getContentType())) {
					logger.warn(file.getOriginalFilename() + " is not an image file, Cannot further process!");
					return new AppBaseResult(false, AppError.Validattion.errorCode(),
							file.getOriginalFilename() + " is not an image file");
				}

				AppUser user = appUserRepository.findByUsername(AppUtil.getCurrentUsername());
				if (user == null) {
					logger.warn("User is not exist, Cannot further process!");
					return new AppBaseResult(false, AppError.Validattion.errorCode(), " User is not exist");
				}

				Path userFolder = Paths.get(FileConstant.USER_FOLDER + AppUtil.getCurrentUsername()).toAbsolutePath()
						.normalize();

				if (!Files.exists(userFolder)) {
					Files.createDirectories(userFolder);
				}

				Files.deleteIfExists(Paths.get(userFolder + user.getUsername() + "." + "jpg"));

				Files.copy(file.getInputStream(), userFolder.resolve(user.getUsername() + "." + "jpg"),
						REPLACE_EXISTING);

				String imageUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path(
						FileConstant.USER_IMAGE_PATH + user.getUsername() + "/" + user.getUsername() + "." + "jpg")
						.toUriString();

				user.getUserInfo().setAvatarImg(imageUrl);
				user.getUserInfo().setUserEdit(AppUtil.getCurrentUsername());

				appUserRepository.save(user);

				return new AppBaseResult(true, AppError.Validattion.errorCode(), imageUrl);
			} else {
				logger.warn("Image file is not null, Cannot further process!");

				return new AppBaseResult(false, AppError.Validattion.errorCode(), "Image file is not null");
			}
		} catch (IOException e) {
			e.printStackTrace();

			return new AppBaseResult(false, AppError.Unknown.errorCode(), AppError.Unknown.errorMessage());
		}
	}

	@Override
	public AppBaseResult resetPassword(String email) {
		try {
			AppUser user = appUserRepository.findByEmail(email);

			if (user == null) {
				logger.warn("Email is not exist: " + email + ", Cannot further process!");
				return new AppBaseResult(false, AppError.Validattion.errorCode(), "Email is not exist: " + email);
			}

			String newPassword = AppUtil.RandomString(15);
			String encodedPassword = bCryptPasswordEncoder.encode(newPassword);
			user.setPassword(encodedPassword);
			
			if(AppUtil.getCurrentUsername() != null) {
				user.setUserEdit(AppUtil.getCurrentUsername());
			}

			appUserRepository.save(user);
			appMailService.sendResetPassword(email, newPassword);
			
			return new AppBaseResult(true, 0, "Success!");
		} catch (Exception e) {
			e.printStackTrace();

			return new AppBaseResult(false, AppError.Unknown.errorCode(), AppError.Unknown.errorMessage());
		}

	}

}
