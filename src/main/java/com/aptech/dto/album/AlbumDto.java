package com.aptech.dto.album;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.aptech.dto.appsatus.AppStatusDto;
import com.aptech.dto.artist.ArtistDto;
import com.aptech.dto.category.CategoryDto;
import com.aptech.dto.genre.GenreDto;
import com.aptech.dto.track.TrackShort;
import com.aptech.entity.Album;
import com.aptech.entity.Artist;
import com.aptech.entity.Genre;
import com.aptech.entity.Track;
import com.aptech.util.AppUtils;

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

	private Integer musicYear;

	private String imgUrl;

	private CategoryDto category;

	private AppStatusDto appStatus;

	private Boolean isActive;

	private List<ArtistDto> singers = new ArrayList<ArtistDto>();

	private List<GenreDto> genres = new ArrayList<GenreDto>();

	private List<TrackShort> tracks = new ArrayList<TrackShort>();

	private Date dateNew;

	private String userNew;

	private Date dateEdit;

	private String userEdit;

	public static AlbumDto CreateFromEntity(Album src) {
		AlbumDto dest = new AlbumDto();

		dest.id = src.getId();
		dest.name = src.getName();
		dest.musicProduction = src.getMusicProduction();
		dest.musicYear = src.getMusicYear();
		dest.imgUrl = AppUtils.createLinkOnCurrentHttpServletRequest(src.getImgUrl());
		dest.category = CategoryDto.CreateFromEntity(src.getCategory());
		dest.appStatus = AppStatusDto.CreateFromEntity(src.getAppStatus());
		dest.isActive = src.getIsActive();
		dest.dateNew = src.getDateNew();
		dest.dateEdit = src.getDateEdit();
		dest.userNew = src.getUserNew();
		dest.userEdit = src.getUserEdit();

		if (src.getSingers() != null)
			for (Artist singer : src.getSingers()) {
				dest.singers.add(ArtistDto.CreateFromEntity(singer));
			}

		if (src.getGenres() != null)
			for (Genre genre : src.getGenres()) {
				dest.genres.add(GenreDto.CreateFromEntity(genre));
			}

		if (src.getTracks() != null)
			for (Track track : src.getTracks()) {
				dest.tracks.add(TrackShort.CreateFromEntity(track));
			}

		return dest;
	}
}
