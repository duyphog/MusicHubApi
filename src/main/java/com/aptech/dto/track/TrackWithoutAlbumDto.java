package com.aptech.dto.track;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.aptech.dto.appsatus.AppStatusDto;
import com.aptech.dto.artist.ComposerDto;
import com.aptech.dto.artist.SingerDto;
import com.aptech.dto.category.CategoryDto;
import com.aptech.dto.genre.GenreDto;
import com.aptech.entity.Artist;
import com.aptech.entity.Genre;
import com.aptech.entity.Track;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrackWithoutAlbumDto {
	private Long id;

	private String name;

	private String musicProduction;

	private Integer musicYear;

	private String lyric;

	private String description;

	private CategoryDto category;

	private AppStatusDto appStatus;

	private Boolean isActive;

	private Long liked;

	private Long listened;

	private String trackUrl;
	
	private Integer durationSeconds;
	
	private Integer bitRate;

	private List<SingerDto> singers = new ArrayList<SingerDto>();

	private List<ComposerDto> composers = new ArrayList<ComposerDto>();

	private List<GenreDto> genres = new ArrayList<GenreDto>();

	private Date dateNew;

	private String userNew;

	private Date dateEdit;

	private String userEdit;

	public static TrackWithoutAlbumDto CreateFromEntity(Track src) {
		TrackWithoutAlbumDto dest = new TrackWithoutAlbumDto();

		dest.id = src.getId();
		dest.name = src.getName();
		dest.musicProduction = src.getMusicProduction();
		dest.musicYear = src.getMusicYear();
		dest.lyric = src.getLyric();
		dest.description = src.getDescription();
		dest.category = CategoryDto.CreateFromEntity(src.getCategory());
		dest.appStatus = AppStatusDto.CreateFromEntity(src.getAppStatus());
		dest.isActive = src.getIsActive();
		dest.liked = src.getLiked();
		dest.listened = src.getListened();
		dest.trackUrl = ServletUriComponentsBuilder.fromCurrentContextPath().path(src.getTrackUrl()).toUriString();
		dest.durationSeconds = src.getDurationSeconds();
		dest.bitRate = src.getBitRate();
		dest.dateNew = src.getDateNew();
		dest.dateEdit = src.getDateEdit();
		dest.userNew = src.getUserNew();
		dest.userEdit = src.getUserEdit();

		if (src.getSingers() != null)
			for (Artist singer : src.getSingers()) {
				dest.singers.add(SingerDto.CreateFromEntity(singer));
			}

		if (src.getComposers() != null)
			for (Artist composer : src.getComposers()) {
				dest.composers.add(ComposerDto.CreateFromEntity(composer));
			}

		if (src.getGenres() != null)
			for (Genre genre : src.getGenres()) {
				dest.genres.add(GenreDto.CreateFromEntity(genre));
			}

		return dest;
	}
}
