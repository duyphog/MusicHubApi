package com.aptech.dto.album;

import java.util.Date;

import com.aptech.entity.Album;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlbumRes {

	private Long id;

	private String name;

	private String description;

	private Date releaseDate;

	private String imageUrl;
	
	public static AlbumRes CreateFromEntity(Album src) {
		AlbumRes dest = new AlbumRes();

		dest.id = src.getId();
		dest.name = src.getName();
		dest.description = src.getDescription();
		dest.releaseDate = src.getReleaseDate();
		dest.imageUrl = src.getImageUrl();

		return dest;
	}
}
