package com.aptech.dto;

import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

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

	public CreateTrack() {
	}

	public CreateTrack(@NotEmpty String name, @NotEmpty String releaseDate, Long composerId, Long genreId,
			@NotEmpty String description, MultipartFile imageFile, MultipartFile trackFile, Long albumId,
			Long artistId) {
		super();
		this.name = name;
		this.releaseDate = releaseDate;
		this.composerId = composerId;
		this.genreId = genreId;
		this.description = description;
		this.imageFile = imageFile;
		this.trackFile = trackFile;
		this.albumId = albumId;
		this.artistId = artistId;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = releaseDate;
	}

	public Long getComposerId() {
		return composerId;
	}

	public void setComposerId(Long composerId) {
		this.composerId = composerId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public MultipartFile getImageFile() {
		return imageFile;
	}

	public void setImageFile(MultipartFile imageFile) {
		this.imageFile = imageFile;
	}

	public MultipartFile getTrackFile() {
		return trackFile;
	}

	public void setTrackFile(MultipartFile trackFile) {
		this.trackFile = trackFile;
	}

	public Long getAlbumId() {
		return albumId;
	}

	public void setAlbumId(Long albumId) {
		this.albumId = albumId;
	}

	public Long getArtistId() {
		return artistId;
	}

	public void setArtistId(Long artistId) {
		this.artistId = artistId;
	}

	public Long getGenreId() {
		return genreId;
	}

	public void setGenreId(Long genreId) {
		this.genreId = genreId;
	}
	
}
