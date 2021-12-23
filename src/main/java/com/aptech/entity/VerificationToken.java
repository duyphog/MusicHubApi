package com.aptech.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "verication_token")
public class VerificationToken implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", updatable = false, nullable = false, columnDefinition = "uniqueidentifier")
	private UUID id;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = false)
	private AppUser appUser;

	@Column(name = "token", columnDefinition = "uniqueidentifier")
	private UUID token;

	@CreationTimestamp
	@Column(name = "date_new")
	private Date dateNew;

	@Column(name = "is_send")
	private boolean isSend;

	@Column(name = "last_send")
	private Date lastTime;

	@Column(name = "is_verify")
	private boolean isVerify;

	@Column(name = "verify_date")
	private Date verifyDate;
}
