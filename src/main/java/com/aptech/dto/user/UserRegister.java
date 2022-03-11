package com.aptech.dto.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
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
}
