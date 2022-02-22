package com.aptech.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "track")
public class Track implements Serializable {

	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name")
	private String name;

	@Column(name = "release_date")
	private Date releaseDate;

	@ManyToOne
	@JoinColumn(name = "composer_id", nullable = true)
	private AppUser composer;
	
	@ManyToOne
	@JoinColumn(name = "genre_id", nullable = false)
	private Genre genre;

	@Column(name = "description")
	private String description;

	@Column(name = "image_url")
	private String imageUrl;
	
	@Column(name = "track_url")
	private String trackUrl;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private AppUser appUser;

	@ManyToOne
	@JoinColumn(name = "album_id", nullable = true)
	private Album album;

	@Column(name = "liked")
	private long liked;

	@Column(name = "listened")
	private long listened;

	@CreationTimestamp
	@Column(name = "date_new")
	private Date dateNew;

	@Column(name = "user_new")
	private String userNew;

	@UpdateTimestamp
	@Column(name = "date_edit")
	private Date dateEdit;

	@Column(name = "user_edit")
	private String userEdit;
}
