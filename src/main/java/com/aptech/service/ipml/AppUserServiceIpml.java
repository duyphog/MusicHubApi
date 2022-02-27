package com.aptech.service.ipml;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aptech.constant.AppError;
import com.aptech.constant.BeanIdConstant;
import com.aptech.constant.FileConstant;
import com.aptech.constant.RoleConstant;
import com.aptech.domain.AppBaseResult;
import com.aptech.domain.AppServiceResult;
import com.aptech.domain.AppUserDomain;
import com.aptech.dto.user.ChangePassword;
import com.aptech.dto.user.UserRegister;
import com.aptech.dto.user.UserWhiteList;
import com.aptech.dto.userinfo.UserInfoDtoReq;
import com.aptech.dto.userinfo.UserInfoDtoRes;
import com.aptech.entity.AppRole;
import com.aptech.entity.AppUser;
import com.aptech.entity.Track;
import com.aptech.entity.UserInfo;
import com.aptech.entity.VerificationToken;
import com.aptech.handle.exception.NotAnImageFileException;
import com.aptech.provider.FileManager;
import com.aptech.repository.AppRoleRepository;
import com.aptech.repository.AppUserRepository;
import com.aptech.repository.TrackRepository;
import com.aptech.repository.VerificationTokenRepository;
import com.aptech.service.IAppUserService;
import com.aptech.service.IAppMailService;
import com.aptech.util.AppUtils;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.SneakyThrows;

@Service
@Qualifier(BeanIdConstant.USER_DETAIL_SERVICE)
public class AppUserServiceIpml implements IAppUserService, UserDetailsService {

	private final Logger logger = LoggerFactory.getLogger(AppUserServiceIpml.class);

	private AppUserRepository appUserRepository;

	private VerificationTokenRepository verificationTokenRepository;

	private AppRoleRepository appRoleRepository;

	private IAppMailService appMailService;

	private TrackRepository trackRepository;

	private BCryptPasswordEncoder bCryptPasswordEncoder;

	private FileManager fileManager;

	@Autowired
	public AppUserServiceIpml(AppUserRepository appUserRepository, AppRoleRepository appRoleRepository,
			VerificationTokenRepository verificationTokenRepository, IAppMailService appMailService,
			TrackRepository trackRepository, BCryptPasswordEncoder bCryptPasswordEncoder, FileManager fileManager) {
		this.appUserRepository = appUserRepository;
		this.verificationTokenRepository = verificationTokenRepository;
		this.appRoleRepository = appRoleRepository;
		this.appMailService = appMailService;
		this.trackRepository = trackRepository;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.fileManager = fileManager;
	}

	@Override
	public AppBaseResult register(UserRegister userRegister) {
		try {
			ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
					false);

			AppUser userByEmail = appUserRepository.findByEmail(userRegister.getEmail());
			if (userByEmail != null) {
				logger.warn("Email is exist: " + userRegister.getEmail() + ", Cannot further process!");

				return AppBaseResult.GenarateIsFailed(AppError.Validattion.errorCode(),
						"Email is exist: " + userRegister.getEmail());
			}

			// TODO: Check email confirm, return message or resend mail

			AppUser userByUsername = appUserRepository.findByUsername(userRegister.getUsername());
			if (userByUsername != null) {
				logger.warn("Username is exist: " + userRegister.getUsername() + ", Cannot further process!");

				return AppBaseResult.GenarateIsFailed(AppError.Validattion.errorCode(),
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

			return AppBaseResult.GenarateIsSucceed();

		} catch (Exception e) {
			e.printStackTrace();

			return AppBaseResult.GenarateIsFailed(AppError.Unknown.errorCode(), AppError.Unknown.errorMessage());
		}
	}

	@Override
	public AppBaseResult verifyEmail(UUID token) {
		VerificationToken vToken = verificationTokenRepository.findByToken(token);

		if (vToken != null) {
			if (vToken.getVerifyDate() != null) {
				logger.warn("Token verified!");

				return AppBaseResult.GenarateIsFailed(AppError.Validattion.errorCode(), "Token verified!");
			}

			vToken.setIsVerify(Boolean.TRUE);
			vToken.setVerifyDate(AppUtils.getNow());

			vToken.getAppUser().setEnabled(Boolean.TRUE);

			verificationTokenRepository.save(vToken);

			return AppBaseResult.GenarateIsSucceed();
		} else {
			logger.warn("Token is not exist: " + token.toString());
			return AppBaseResult.GenarateIsFailed(AppError.Unknown.errorCode(), "Token is not exist!");
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
				return new AppServiceResult<UserInfoDtoRes>(false, AppError.Validattion.errorCode(),
						"User is not exist!", null);
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
			String currentUsername = AppUtils.getCurrentUsername();

			if (currentUsername != null) {

				AppUser user = appUserRepository.findByUsername(currentUsername);
				if (userInfo.getUserId() != user.getId()) {
					logger.warn("Not match UserId, Cannot further process!");

					return AppBaseResult.GenarateIsFailed(AppError.Validattion.errorCode(), "User is not match id");
				}

				// TODO: Implement mapping
				user.setEmail(userInfo.getEmail());
				user.getUserInfo().setFirstName(userInfo.getFirstName());
				user.getUserInfo().setLastName(userInfo.getLastName());
				user.getUserInfo().setStory(userInfo.getStory());

				user.getUserInfo().setUserEdit(currentUsername);

				// Save user
				appUserRepository.save(user);

				return AppBaseResult.GenarateIsSucceed();
			}
		} catch (Exception e) {
			e.printStackTrace();

			return AppBaseResult.GenarateIsFailed(AppError.Unknown.errorCode(), AppError.Unknown.errorMessage());
		}

		return AppBaseResult.GenarateIsFailed(AppError.Unknown.errorCode(), AppError.Unknown.errorMessage());
	}

	@Override
	public AppBaseResult changePassword(ChangePassword changePassword) {
		try {
			String currentUsername = AppUtils.getCurrentUsername();

			if (!currentUsername.equals(changePassword.getUsername())) {
				logger.warn("Not match UserId, Cannot further process!");

				return AppBaseResult.GenarateIsFailed(AppError.Validattion.errorCode(), "Not match UserId");
			}

			if (changePassword.getNewPassword().equals(changePassword.getOldPassword())) {
				logger.warn("New password euqals old password, Cannot further process!");

				return AppBaseResult.GenarateIsFailed(AppError.Validattion.errorCode(),
						"New password euqals old password");
			}

			AppUser user = appUserRepository.findByUsername(currentUsername);

			if (user == null) {
				logger.warn("User is not exist!, Cannot further process!");

				return AppBaseResult.GenarateIsFailed(AppError.Validattion.errorCode(), "User is not exist!");
			}

			if (!bCryptPasswordEncoder.matches(changePassword.getOldPassword(), user.getPassword())) {
				logger.warn("Password incorrect, Cannot further process!");

				return AppBaseResult.GenarateIsFailed(AppError.Validattion.errorCode(), "Password incorrect!");
			}

			user.setPassword(bCryptPasswordEncoder.encode(changePassword.getNewPassword()));
			user.setUserEdit(currentUsername);

			appUserRepository.save(user);

			return AppBaseResult.GenarateIsSucceed();
		} catch (Exception e) {
			e.printStackTrace();

			return AppBaseResult.GenarateIsFailed(AppError.Unknown.errorCode(), AppError.Unknown.errorMessage());
		}

	}

	@Override
	public AppServiceResult<String> uploadImage(MultipartFile file) throws NotAnImageFileException {
		try {
			if (file != null) {

				AppUser user = appUserRepository.findByUsername(AppUtils.getCurrentUsername());
				if (user == null) {
					logger.warn("User is not exist, Cannot further process!");

					return new AppServiceResult<String>(false, AppError.Validattion.errorCode(), " User is not exist",
							null);
				}

				Path userFolder = Paths.get(FileConstant.USER_IMAGE_FOLDER + AppUtils.getCurrentUsername())
						.toAbsolutePath().normalize();

				String imageUrl = fileManager.uploadUserImage(userFolder, user.getUsername(), file);

				user.getUserInfo().setAvatarImg(imageUrl);
				user.getUserInfo().setUserEdit(AppUtils.getCurrentUsername());

				appUserRepository.save(user);

				return new AppServiceResult<String>(true, 0, "Succeed!", imageUrl);
			} else {
				logger.warn("Image file is not null, Cannot further process!");

				return new AppServiceResult<String>(false, AppError.Validattion.errorCode(), "Image file is not null",
						null);
			}
		} catch (IOException e) {
			e.printStackTrace();

			return new AppServiceResult<String>(false, AppError.Unknown.errorCode(), AppError.Unknown.errorMessage(),
					null);
		} catch (NotAnImageFileException e) {

			e.printStackTrace();
			throw e;
		}
	}

	@Override
	@Transactional
	public AppBaseResult resetPassword(String email) {
		try {
			AppUser user = appUserRepository.findByEmail(email);

			if (user == null) {
				logger.warn("Email is not exist: " + email + ", Cannot further process!");

				return AppBaseResult.GenarateIsFailed(AppError.Validattion.errorCode(), "Email is not exist: " + email);
			}

			String newPassword = AppUtils.RandomString(15);
			String encodedPassword = bCryptPasswordEncoder.encode(newPassword);
			user.setPassword(encodedPassword);

			if (AppUtils.getCurrentUsername() != null) {
				user.setUserEdit(AppUtils.getCurrentUsername());
			}

			appUserRepository.save(user);
			appMailService.sendResetPassword(email, newPassword);

			return AppBaseResult.GenarateIsSucceed();
		} catch (Exception e) {
			e.printStackTrace();

			return AppBaseResult.GenarateIsFailed(AppError.Validattion.errorCode(), "Email is not exist: " + email);
		}

	}

	@Override
	public AppBaseResult updateWhiteList(UserWhiteList dto) {
		try {
			AppUser user = appUserRepository.findByUsername(AppUtils.getCurrentUsername());

			Track track = trackRepository.findById(dto.getTrackId()).orElse(null);
			
			if(track == null) {
				logger.warn("TrackId is not exist: " + dto.getTrackId() + ", Cannot further process!");

				return AppBaseResult.GenarateIsFailed(AppError.Validattion.errorCode(), "TrackId is not exist: " + dto.getTrackId());
			}

			if(dto.getIsAdd())
				user.getWhiteList().add(track);
			else
				user.getWhiteList().removeIf(item -> item.getId() == dto.getTrackId());
			
			appUserRepository.save(user);
			
			return AppBaseResult.GenarateIsSucceed();
		} catch (Exception e) {
			e.printStackTrace();

			return AppBaseResult.GenarateIsFailed(AppError.Unknown.errorCode(), AppError.Unknown.errorMessage());
		}
	}

}
