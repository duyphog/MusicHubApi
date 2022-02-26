package com.aptech.dto.artist;

import java.util.Date;

import com.aptech.entity.Artist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComposerDto {

	private Long id;

	private String nickName;

//	private Date birthday;
//
//	private boolean gender;
//
//	private String avatarImgUrl;
//
//	private String coverImgUrl;

	public static ComposerDto CreateFromEntity(Artist src) {
		ComposerDto dest = new ComposerDto();

		dest.id = src.getId();
		dest.nickName = src.getNickName();
//		dest.birthday = src.getBirthday();
//		dest.gender = src.isGender();
//		dest.avatarImgUrl = src.getAvatarImgUrl();
//		dest.coverImgUrl = src.getCoverImgUrl();

		return dest;
	}
}
