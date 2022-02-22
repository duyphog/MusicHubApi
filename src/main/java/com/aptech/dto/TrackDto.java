package com.aptech.dto;

import java.util.Date;

public class TrackDto {

	private long id;
	private String name;
	private Date releaseDate;
	private String composer;
	private long composerId;
	private GenreDto Genre;
	private String description;
	private String imageUrl;
	private String trackUrl;
	private AlbumRes album;
	private long liked;
	private long listened;

	public TrackDto() {
	}

	public TrackDto(long id, String name, Date releaseDate, String composer, long composerId, GenreDto genre,
			String description, String imageUrl, String trackUrl, AlbumRes album, long liked, long listened) {
		super();
		this.id = id;
		this.name = name;
		this.releaseDate = releaseDate;
		this.composer = composer;
		this.composerId = composerId;
		Genre = genre;
		this.description = description;
		this.imageUrl = imageUrl;
		this.trackUrl = trackUrl;
		this.album = album;
		this.liked = liked;
		this.listened = listened;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getReleaseDate() {
		return releaseDate;
	}

	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	public String getComposer() {
		return composer;
	}

	public void setComposer(String composer) {
		this.composer = composer;
	}

	public long getComposerId() {
		return composerId;
	}

	public void setComposerId(long composerId) {
		this.composerId = composerId;
	}

	public GenreDto getGenre() {
		return Genre;
	}

	public void setGenre(GenreDto genre) {
		Genre = genre;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getTrackUrl() {
		return trackUrl;
	}

	public void setTrackUrl(String trackUrl) {
		this.trackUrl = trackUrl;
	}

	public AlbumRes getAlbum() {
		return album;
	}

	public void setAlbum(AlbumRes album) {
		this.album = album;
	}

	public long getLiked() {
		return liked;
	}

	public void setLiked(long liked) {
		this.liked = liked;
	}

	public long getListened() {
		return listened;
	}

	public void setListened(long listened) {
		this.listened = listened;
	}
}
