package com.aptech.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MetaData {
	private Integer discNumber;
	private Integer trackNumber;
	private String title;
	private String composer;
	private String producer;
	private String artist;
	private String albumArtist;
	private String albumName;
	private String genre;
	private String lyric;
	private Integer year;
	private Integer bitRate;
	private boolean variableBitRate;
	private Integer durationSeconds;
	private Integer width;
	private Integer height;
	private String musicBrainzReleaseId;
	private String musicBrainzRecordingId;
}
