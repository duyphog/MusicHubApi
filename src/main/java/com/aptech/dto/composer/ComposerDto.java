package com.aptech.dto.composer;

import com.aptech.entity.AppUser;
import com.aptech.entity.Singer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComposerDto {
	private long id;

	private String stageName;

	private String avatarUrl;

	public static ComposerDto CreateFromEntity(AppUser src) {
		ComposerDto dest = new ComposerDto();

		dest.id = src.getId();
		dest.stageName = src.getUserInfo().getLastName();
//		dest.avatarUrl = src.getAvatarUrl();

		return dest;
	}
}
