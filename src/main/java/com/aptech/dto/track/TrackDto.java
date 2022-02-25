package com.aptech.dto.track;

import java.util.Date;

import com.aptech.dto.genre.GenreDto;
import com.aptech.entity.Track;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrackDto {
	private Long id;
	private String name;
	private Date releaseDate;
//	private AbumDto album;
//	private ComposerDto composer;
	private GenreDto genre;
	private String description;
	private String imageUrl;
	private String trackUrl;
	private long liked;
	private long listened;
	
	public static TrackDto CreateFromEntity(Track src) {
		TrackDto dest = new TrackDto();
		
		dest.id = src.getId();
		dest.name = src.getName();
		dest.releaseDate = src.getReleaseDate();
//		dest.album = AbumDto.CreateFromEntity(src.getAlbum());
//		dest.composer = ComposerDto.CreateFromEntity(src.getComposer());
		dest.genre = GenreDto.CreateFromEntity(src.getGenre());
		dest.description = src.getDescription();
		dest.imageUrl = src.getImageUrl();
		dest.trackUrl = src.getTrackUrl();
		dest.liked = src.getLiked();
		dest.listened = src.getListened();
		
		return dest;
	}
}
