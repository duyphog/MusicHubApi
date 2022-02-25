package com.aptech.dto.album;

import java.util.ArrayList;
import java.util.List;

import com.aptech.dto.appsatus.AppStatusDto;
import com.aptech.dto.artist.ArtistDto;
import com.aptech.dto.category.CategoryDto;
import com.aptech.dto.genre.GenreDto;
import com.aptech.entity.Album;
import com.aptech.entity.Artist;
import com.aptech.entity.Genre;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlbumDto {

	private Long id;

	private String name;

	private String musicProduction;

	private int musicYear;

	private String imgUrl;

	private CategoryDto category;

	private AppStatusDto appStatus;

	private boolean isActive;

	private List<ArtistDto> singers = new ArrayList<ArtistDto>();

	private List<GenreDto> genres = new ArrayList<GenreDto>();

	public static AlbumDto CreateFromEntity(Album src) {
		AlbumDto dest = new AlbumDto();

		dest.id = src.getId();
		dest.name = src.getName();
		dest.musicProduction = src.getMusicProduction();
		dest.musicYear = src.getMusicYear();
		dest.imgUrl = src.getImgUrl();
		dest.category = CategoryDto.CreateFromEntity(src.getCategory());
		dest.appStatus = AppStatusDto.CreateFromEntity(src.getAppStatus());
		dest.isActive = src.isActive();

		for (Artist singer : src.getSingers()) {
			dest.singers.add(ArtistDto.CreateFromEntity(singer));
		}

		for (Genre genre : src.getGenres()) {
			dest.genres.add(GenreDto.CreateFromEntity(genre));
		}

		return dest;
	}
}
