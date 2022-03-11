package com.aptech.dto.user;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.aptech.dto.userinfo.UserInfoForAdminDtoRes;
import com.aptech.entity.AppUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppUserForAdminDto {
	private Long id;
	
	private String username;
	
	private String email;
	
	private Boolean enabled;
	
	private List<AppRoleDto> appRoles = new ArrayList<AppRoleDto>();
	
	private Boolean accountNonLocked;
	
	private UserInfoForAdminDtoRes userInfo;
	
	private Date dateNew;

	private String userNew;

	private Date dateEdit;

	private String userEdit;
	
	public static AppUserForAdminDto CreateFromEntity(AppUser src) {
		AppUserForAdminDto dest = new AppUserForAdminDto();

		dest.id = src.getId();
		dest.username = src.getUsername();
		dest.email = src.getEmail();
		dest.enabled = src.getEnabled();
		dest.accountNonLocked = src.getAccountNonLocked();
		dest.dateNew = src.getDateNew();
		dest.dateEdit = src.getDateEdit();
		dest.userNew = src.getUserNew();
		dest.userEdit = src.getUserEdit();
			
		if (src.getUserInfo() != null)
			dest.userInfo = UserInfoForAdminDtoRes.CreateFromEntity(src.getUserInfo());

		if (src.getAppRoles() != null)
			src.getAppRoles().forEach(item -> dest.appRoles.add(AppRoleDto.CreateFromEntity(item)));

		return dest;
	}
}
