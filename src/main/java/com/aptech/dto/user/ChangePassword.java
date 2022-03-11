package com.aptech.dto.user;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
}
