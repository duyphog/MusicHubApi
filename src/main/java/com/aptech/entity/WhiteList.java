package com.aptech.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_track")
public class WhiteList implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
    private WhiteListId whiteListId;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("userId")
	@JoinColumn(name = "user_id")
	private AppUser appUser;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("trackId")
	@JoinColumn(name = "track_id")
	private Track track;

	@CreationTimestamp
	@Column(name = "date_new")
	private Date dateNew;
}
