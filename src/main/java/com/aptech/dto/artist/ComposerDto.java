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
public class ComposerDto {

	private Long id;

	private String nickName;

	public static ComposerDto CreateFromEntity(Artist src) {
		ComposerDto dest = new ComposerDto();

		dest.id = src.getId();
		dest.nickName = src.getNickName();

		return dest;
	}
}
