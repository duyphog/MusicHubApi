package com.aptech.dto.user;

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
public class UserInfoDto {
	  private Long userId;
	  private String username;
	  private String firstName;
	  private String lastName;
	  private String email;
	  private String story;
	  private String avatarImg;

	public static UserInfoDto CreateFromEntity(UserInfo src) {
		UserInfoDto dest = new UserInfoDto();

		dest.userId = src.getAppUser().getId();
		dest.username = src.getAppUser().getUsername();
		dest.firstName = src.getFirstName();
		dest.lastName = src.getLastName();
		dest.avatarImg = AppUtils.createLinkOnCurrentHttpServletRequest(src.getAvatarImg());
		dest.story = src.getStory();
		dest.email = src.getAppUser().getEmail();

		return dest;
	}
}
