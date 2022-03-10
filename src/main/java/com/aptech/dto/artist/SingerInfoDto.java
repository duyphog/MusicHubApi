package com.aptech.dto.artist;

import java.util.Date;

import com.aptech.entity.Artist;
import com.aptech.util.AppUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SingerInfoDto {

	private Long id;

	private String nickName;

	private Date birthday;

	private boolean gender;

	private String avatarImgUrl;

	private String coverImgUrl;

	public static SingerInfoDto CreateFromEntity(Artist src) {
		SingerInfoDto dest = new SingerInfoDto();

		dest.id = src.getId();
		dest.nickName = src.getNickName();
		dest.birthday = src.getBirthday();
		dest.gender = src.getGender();
		dest.avatarImgUrl = AppUtils.createLinkOnCurrentHttpServletRequest(src.getAvatarImgUrl());
		dest.coverImgUrl = AppUtils.createLinkOnCurrentHttpServletRequest(src.getCoverImgUrl());

		return dest;
	}
}
