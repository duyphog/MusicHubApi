package com.aptech.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class ChangePassword {

	@NotEmpty
	@Size(min = 2, message = "user name should have at least 2 characters")
	private String username;

	@NotEmpty
	@Size(min = 8, message = "password should have at least 8 characters")
	private String oldPassword;
	
	@NotEmpty
	@Size(min = 8, message = "password should have at least 8 characters")
	private String newPassword;


	public ChangePassword() {
		
	}
			
	public ChangePassword(
			@NotEmpty @Size(min = 2, message = "user name should have at least 2 characters") String username,
			@NotEmpty @Size(min = 8, message = "password should have at least 8 characters") String oldPassword,
			@NotEmpty @Size(min = 8, message = "password should have at least 8 characters") String newPassword) {
		super();
		this.username = username;
		this.oldPassword = oldPassword;
		this.newPassword = newPassword;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
}
