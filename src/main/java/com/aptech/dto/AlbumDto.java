package com.aptech.dto;

import java.util.Date;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

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

	public AlbumDto() {
	}

	public AlbumDto(String name, String description, Date releaseDate, MultipartFile imageFile, Long artistId) {
		this.name = name;
		this.description = description;
		this.releaseDate = releaseDate;
		this.imageFile = imageFile;
		this.artistId = artistId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public MultipartFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(MultipartFile imageFile) {
		this.imageFile = imageFile;
	}

	public Long getArtistId() {
		return artistId;
	}

	public void setArtistId(Long artistId) {
		this.artistId = artistId;
	}
}
