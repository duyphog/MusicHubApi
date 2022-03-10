package com.aptech.dto.track;

import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TrackCreate {

	@NotEmpty
	private String name;

	private Long albumId;

	private String musicProduction;

	private Integer musicYear;

	private String lyric;

	private String description;

	private Long categoryId;

	@NotEmpty
	private Long[] singerIds;

	private Long[] composerIds;

	private Long[] genreIds;

	private MultipartFile trackFile;
}
