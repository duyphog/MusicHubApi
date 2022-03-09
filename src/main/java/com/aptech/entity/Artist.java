package com.aptech.entity;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
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
@Table(name = "artist")
public class Artist implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Field(index= Index.YES, analyze= Analyze.YES, store= Store.NO)
	@Column(name = "nick_name")
	private String nickName;

	@Column(name = "birthday")
	private Date birthday;

	@Column(name = "gender")
	private Boolean gender;

	@Column(name = "avatar_img_url")
	private String avatarImgUrl;

	@Column(name = "cover_img_url")
	private String coverImgUrl;

	@Column(name = "is_composer")
	private Boolean isComposer;

	@Column(name = "is_singer")
	private Boolean isSinger;

	@Column(name = "is_active")
	private Boolean isActive;
}
