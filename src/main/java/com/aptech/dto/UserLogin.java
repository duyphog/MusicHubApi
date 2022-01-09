package com.aptech.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class UserLogin {
	
	@NotEmpty
	@Size(min = 2, message = "user name should have at least 2 characters")
	private String username;
	
	@NotEmpty
	@Size(min = 8, message = "password should have at least 8 characters")
	private String password;
	
	public UserLogin() {
		
	}
	
	public UserLogin(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
