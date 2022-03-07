package com.aptech.dto.album;

import java.util.ArrayList;
import java.util.List;

import com.aptech.dto.artist.SingerDto;
import com.aptech.dto.category.CategoryDto;
import com.aptech.dto.genre.GenreDto;
import com.aptech.entity.Album;
import com.aptech.entity.Artist;
import com.aptech.entity.Genre;
import com.aptech.util.AppUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlbumShort {
	
	private Long id;

	private String name;

	private String imgUrl;

	private List<SingerDto> singers = new ArrayList<SingerDto>();

	public static AlbumShort CreateFromEntity(Album src) {
		AlbumShort dest = new AlbumShort();

		dest.id = src.getId();
		dest.name = src.getName();
		dest.imgUrl = AppUtils.createLinkOnCurrentHttpServletRequest(src.getImgUrl());

		if (src.getSingers() != null)
			for (Artist singer : src.getSingers()) {
				dest.singers.add(SingerDto.CreateFromEntity(singer));
			}

		return dest;
	}
}
