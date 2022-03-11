package com.aptech.dto.playlist;

import com.aptech.entity.PlaylistType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistTypeDto {
	private Long id;

	private String name;

	public static PlaylistTypeDto CreateFromEntity(PlaylistType src) {
		PlaylistTypeDto dest = new PlaylistTypeDto();

		dest.id = src.getId();
		dest.name = src.getName();

		return dest;
	}
}
