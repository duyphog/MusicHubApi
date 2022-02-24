package com.aptech.dto.album;

import java.util.Date;

import javax.validation.constraints.Email;
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
public class AlbumDto {

	@NotEmpty
	private String name;

	@Email
	@NotEmpty
	private String description;

	@NotEmpty
	private Date releaseDate;

	private MultipartFile imageFile;

	private Long artistId;

	private MultipartFile[] trackFiles;

}
