package com.aptech.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
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
}
