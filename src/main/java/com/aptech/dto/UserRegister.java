package com.aptech.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class UserRegister {

	@NotEmpty
	@Size(min = 2, message = "user name should have at least 2 characters")
	private String username;

	@NotEmpty
	@Email
	private String email;

	@NotEmpty
	@Size(min = 8, message = "password should have at least 8 characters")
	private String password;

	public UserRegister() {
		
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
