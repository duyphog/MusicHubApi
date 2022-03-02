package com.aptech.dto.playlist;

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
public class PlaylistCreate {

	@NotNull
	private String name;

	private String description;

	private Long categoryId;

	private Long genreId;

	private Long playlistTypeId;
	
	private MultipartFile imgFile;
}
