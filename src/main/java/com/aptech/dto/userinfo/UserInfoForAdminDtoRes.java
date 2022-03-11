package com.aptech.dto.userinfo;

import com.aptech.entity.UserInfo;
import com.aptech.util.AppUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoForAdminDtoRes {

	private String username;

	private String firstName;

	private String lastName;

	private String email;

	private String avatarImg;

	private String story;
	
	public static UserInfoForAdminDtoRes CreateFromEntity(UserInfo src) {
		UserInfoForAdminDtoRes dest = new UserInfoForAdminDtoRes();

		dest.username = src.getAppUser().getUsername();
		dest.firstName = src.getFirstName();
		dest.lastName = src.getLastName();
		dest.avatarImg = AppUtils.createLinkOnCurrentHttpServletRequest(src.getAvatarImg());
		dest.story = src.getStory();
		dest.email = src.getAppUser().getEmail();

		return dest;
	}
}
