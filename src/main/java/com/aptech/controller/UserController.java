package com.aptech.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.UUID;

import javax.validation.Valid;

import com.aptech.entity.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aptech.constant.FileConstant;
import com.aptech.domain.AppBaseResult;
import com.aptech.domain.AppServiceResult;
import com.aptech.domain.AppUserDomain;
import com.aptech.dto.HttpResponse;
import com.aptech.dto.HttpResponseError;
import com.aptech.dto.HttpResponseSuccess;
import com.aptech.dto.user.ChangePassword;
import com.aptech.dto.user.UserLogin;
import com.aptech.dto.user.UserLoginRes;
import com.aptech.dto.user.UserRegister;
import com.aptech.dto.user.UserStatus;
import com.aptech.dto.user.UserWhiteList;
import com.aptech.dto.userinfo.UserInfoDtoReq;
import com.aptech.dto.userinfo.UserInfoDtoRes;
import com.aptech.infrastructure.AppJwtTokenProvider;
import com.aptech.provider.file.UnsupportedFileTypeException;
import com.aptech.service.AppUserService;

import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

@RestController
@RequestMapping("/user")
public class UserController {

	private AppUserService appUserService;

	private AuthenticationManager authenticationManager;

	private AppJwtTokenProvider appJwtTokenProvider;

	@Value("${app.config.url.loginapp}")
	private String urlLoginApp;

	@Autowired
	public UserController(AppUserService appUserService, AuthenticationManager authenticationManager,
			AppJwtTokenProvider appJwtTokenProvider) {
		this.appJwtTokenProvider = appJwtTokenProvider;
		this.authenticationManager = authenticationManager;
		this.appUserService = appUserService;
	}

	@PostMapping("/register")
	public ResponseEntity<HttpResponse> register(@Valid @RequestBody UserRegister userRegister) {

		AppBaseResult result = appUserService.register(userRegister);

		return result.isSuccess()
				? ResponseEntity.ok(new HttpResponseSuccess<String>("Register succeed!, please verify email to login!"))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}

	@GetMapping("/verify/{token}")
	public ResponseEntity<Void> verifyEmail(@PathVariable(name = "token", required = true) UUID token) {

		AppBaseResult result = appUserService.verifyEmail(token);
		String url = urlLoginApp + (result.isSuccess() ? "=success" : "=fail");

		return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(url)).build();
	}

	@PostMapping("/login")
	public ResponseEntity<HttpResponse> login(@Valid @RequestBody UserLogin userLogin) {

		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(userLogin.getUsername(), userLogin.getPassword()));

		AppUserDomain appUserDetails = (AppUserDomain) authentication.getPrincipal();

		String userToken = appJwtTokenProvider.generateJwtToken(appUserDetails);
		UserLoginRes res = new UserLoginRes(appUserDetails.getUserId(), appUserDetails.getUsername(), userToken);

		return ResponseEntity.ok(new HttpResponseSuccess<UserLoginRes>(res));
	}

	@GetMapping
	public ResponseEntity<HttpResponse> getUsers() {

		AppServiceResult<List<AppUser>> result = appUserService.getUsers();
		System.out.println(result.getData());
		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<List<AppUser>>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}

	@GetMapping("/profiles")
	public ResponseEntity<HttpResponse> getProfiles(@Valid @RequestParam(value = "id") Long userId) {

		AppServiceResult<UserInfoDtoRes> result = appUserService.getProfile(userId);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<UserInfoDtoRes>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}

	@PutMapping("/profiles")
	public ResponseEntity<HttpResponse> saveProfiles(@Valid @RequestBody UserInfoDtoReq userInfo) {

		AppBaseResult result = appUserService.saveProfile(userInfo);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<String>("Success!"))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}

	@PutMapping("/password")
	public ResponseEntity<HttpResponse> changePassword(@Valid @RequestBody ChangePassword changePassword) {

		AppBaseResult result = appUserService.changePassword(changePassword);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<String>("Success!"))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}

	@PostMapping("/upload-profile-image")
	public ResponseEntity<HttpResponse> uploadImage(@RequestParam("profileImage") MultipartFile file)
			throws UnsupportedFileTypeException {

		AppServiceResult<String> result = appUserService.uploadImage(file);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<String>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}

	@GetMapping(path = "/image/profile/{username}", produces = IMAGE_JPEG_VALUE)
	public byte[] getTempProfileImage(@PathVariable("username") String username) throws IOException {
		URL url = new URL(FileConstant.TEMP_PROFILE_IMAGE_BASE_URL + username);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		try (InputStream inputStream = url.openStream()) {
			byte[] chunk = new byte[1024];
			int bytesRead;
			while ((bytesRead = inputStream.read(chunk)) > 0) {
				byteArrayOutputStream.write(chunk, 0, bytesRead);
			}
		}
		return byteArrayOutputStream.toByteArray();
	}

	@PostMapping("/reset-password/{email}")
	public ResponseEntity<HttpResponse> uploadImage(@PathVariable("email") String email) {

		AppBaseResult result = appUserService.resetPassword(email);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<String>("Succeed!, please check email!"))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}

	@PutMapping("/white-list")
	public ResponseEntity<HttpResponse> updateWhiteList(@Valid @RequestBody UserWhiteList dto) {

		AppBaseResult result = appUserService.updateWhiteList(dto);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<String>("Succeed!"))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
	
	@PostMapping("/update-status")
	public ResponseEntity<HttpResponse> updateStatus(@Valid @RequestBody UserStatus userStatus) {

		AppBaseResult result = appUserService.updateActive(userStatus);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<String>("Succeed!"))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
}
