package com.aptech.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_role")
public class AppRole implements Serializable {

	private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "name")
	private String name;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "appRoles")
	private Set<AppUser> appUsers = new HashSet<>();
	
//    @JoinTable(name = "role_authority",
//            joinColumns = @JoinColumn(name = "role_id"),
//            inverseJoinColumns = @JoinColumn(name = "authority_id"))
//    private Set<Authority> authorities;
}
