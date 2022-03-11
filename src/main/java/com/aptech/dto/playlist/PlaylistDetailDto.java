package com.aptech.dto.playlist;

import java.util.Date;

import com.aptech.dto.track.TrackShort;
import com.aptech.entity.PlaylistDetail;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistDetailDto {
	private Long id;

	private TrackShort track;

	private Date dateNew;

	public static PlaylistDetailDto CreateFromEntity(PlaylistDetail src) {
		PlaylistDetailDto dest = new PlaylistDetailDto();

		dest.id = src.getId();
		dest.dateNew = src.getDateNew();

		if (src.getTrack() != null)
			dest.track = TrackShort.CreateFromEntity(src.getTrack());

		return dest;
	}
}
