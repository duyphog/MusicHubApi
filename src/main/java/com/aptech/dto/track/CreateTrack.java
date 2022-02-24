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
public class CreateTrack {

	@NotEmpty
	private String name;

	@NotEmpty
	private String releaseDate;

	private Long composerId;

	private Long genreId;

	@NotEmpty
	private String description;

	private MultipartFile imageFile;

	private MultipartFile trackFile;

	private Long albumId;

	private Long artistId;

	private Long[] singerIds;
}
