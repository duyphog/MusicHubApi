package com.aptech.dto.singer;

import com.aptech.entity.Singer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SingerDto {
	private long id;
	private String stageName;
	private String avatarUrl;

	public static SingerDto CreateFromEntity(Singer src) {
		SingerDto dest = new SingerDto();

		dest.id = src.getId();
		dest.stageName = src.getStageName();
		dest.avatarUrl = src.getAvatarUrl();

		return dest;
	}
}
