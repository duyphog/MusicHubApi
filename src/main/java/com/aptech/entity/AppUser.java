package com.aptech.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
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
@Table(name = "app_user")
public class AppUser implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "id", updatable = false, nullable = false, columnDefinition = "uniqueidentifier")
	private UUID id;

	@Column(name = "username")
	private String username;

	@Column(name = "password")
	private String password;

	@Column(name = "email")
	private String email;
	
	@Column(name = "enabled")
	private boolean enabled;
	
	@Column(name = "account_non_locked")
	private boolean accountNonLocked;

	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "app_user_role", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
			@JoinColumn(name = "role_id") })
	private Set<AppRole> appRoles = new HashSet<>();
	
	@ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinTable(name = "app_user_authority", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = {
			@JoinColumn(name = "authority_id") })
    private Set<AppAuthority> authorities;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "userinfo_id", referencedColumnName = "id")
	private UserInfo userInfo;

	@OneToMany(mappedBy = "appUser")
	private Set<VerificationToken> verificationTokens;
	
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
