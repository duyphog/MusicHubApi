package com.aptech.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.aptech.entity.AppUser;

public class AppUserDomain implements UserDetails {

	private static final long serialVersionUID = 1L;

	private AppUser appUser;
	
	public AppUserDomain() {
		super();
	}
	
	public AppUserDomain(AppUser appUser) {
		super();
		this.appUser = appUser;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> grantedAuthorityList = new ArrayList<GrantedAuthority>();
		
		grantedAuthorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
		
		return grantedAuthorityList;
	}

	@Override
	public String getPassword() {
		return this.appUser.getPassword();
	}

	@Override
	public String getUsername() {
		return this.appUser.getUsername();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.appUser.isAccountNonLocked();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.appUser.isEnabled();
	}
	
	public String getEmail() {
		return this.appUser.getEmail();
	}
	
	public Long getUserId() {
		return this.appUser.getId();
	}

}
