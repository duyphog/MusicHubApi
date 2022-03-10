package com.aptech.dto.artist;

import com.aptech.entity.Artist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SingerDto {

	private Long id;

	private String nickName;

	public static SingerDto CreateFromEntity(Artist src) {
		SingerDto dest = new SingerDto();

		dest.id = src.getId();
		dest.nickName = src.getNickName();

		return dest;
	}
}
