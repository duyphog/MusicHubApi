package com.aptech.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Indexed
@Table(name = "track")
public class Track implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Field(index= Index.YES, analyze= Analyze.YES, store= Store.NO)
	@Column(name = "name")
	private String name;

	@IndexedEmbedded
	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "album_id", nullable = true)
	private Album album;
	
	@Column(name = "music_production")
	private String musicProduction;

	@Column(name = "music_year")
	private Integer musicYear;
	
	@Column(name = "lyric")
	private String lyric;

	@Column(name = "description")
	private String description;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "user_id", nullable = false)
	private AppUser appUser;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "category_id", nullable = false)
	private Category category;

	@ManyToOne(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinColumn(name = "status_id", nullable = false)
	private AppStatus appStatus;

	@Column(name = "is_active")
	private Boolean isActive;

	@Column(name = "liked")
	private Long liked;

	@Column(name = "listened")
	private Long listened;

	@Column(name = "track_url")
	private String trackUrl;
	
	@Column(name = "track_path")
	private String trackPath;
	
	@Column(name = "duration_seconds", nullable = false)
	private Integer durationSeconds;
	
	@Column(name = "bit_rate", nullable = false)
	private Integer bitRate;

	@IndexedEmbedded
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinTable(name = "track_singer", joinColumns = { @JoinColumn(name = "track_id") }, inverseJoinColumns = {
			@JoinColumn(name = "artist_id") })
	private Set<Artist> singers = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinTable(name = "track_composer", joinColumns = { @JoinColumn(name = "track_id") }, inverseJoinColumns = {
			@JoinColumn(name = "artist_id") })
	private Set<Artist> composers = new HashSet<>();

	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH })
	@JoinTable(name = "track_genre", joinColumns = { @JoinColumn(name = "track_id") }, inverseJoinColumns = {
			@JoinColumn(name = "genre_id") })
	private Set<Genre> genres = new HashSet<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "track")
	private Set<PlaylistDetail> playlistDetails = new HashSet<>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "track")
	private Set<WhiteList> whiteLists = new HashSet<>();
  
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
