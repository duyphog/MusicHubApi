package com.aptech.dto.album;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlbumCreate {

	@NotEmpty
	private String name;

	private String musicProduction;

	@NotNull
	private Integer musicYear;

	@NotEmpty
	private MultipartFile imgFile;

	@NotEmpty
	private Long categoryId;

	@NotEmpty
	private Long[] singerIds;

	@NotEmpty
	private Long[] genreIds;
}
