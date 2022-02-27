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
public class ArtistDto {

	private Long id;

	private String nickName;

	private Date birthday;

	private Boolean gender;

	private String avatarImgUrl;

	private String coverImgUrl;

	private Boolean isComposer;

	private Boolean isSinger;

	private Boolean isActive;

	public static ArtistDto CreateFromEntity(Artist src) {
		ArtistDto dest = new ArtistDto();

		dest.id = src.getId();
		dest.nickName = src.getNickName();
		dest.birthday = src.getBirthday();
		dest.gender = src.getGender();
		dest.avatarImgUrl = src.getAvatarImgUrl();
		dest.coverImgUrl = src.getCoverImgUrl();
		dest.isComposer = src.getIsComposer();
		dest.isSinger = src.getIsSinger();

		return dest;
	}
}
