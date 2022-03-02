package com.aptech.dto.track;

import java.util.ArrayList;
import java.util.List;

import com.aptech.dto.artist.SingerDto;
import com.aptech.entity.Artist;
import com.aptech.entity.Track;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrackShort {
	private Long id;

	private String name;

	private Long liked;

	private Long listened;
	
	private Integer durationSeconds;
	
	private Integer bitRate;

	private List<SingerDto> singers = new ArrayList<SingerDto>();

	public static TrackShort CreateFromEntity(Track src) {
		TrackShort dest = new TrackShort();

		dest.id = src.getId();
		dest.name = src.getName();
		dest.liked = src.getLiked();
		dest.listened = src.getListened();
		dest.durationSeconds = src.getDurationSeconds();
		dest.bitRate = src.getBitRate();
		
		if (src.getSingers() != null)
			for (Artist singer : src.getSingers()) {
				dest.singers.add(SingerDto.CreateFromEntity(singer));
			}

		return dest;
	}
}
