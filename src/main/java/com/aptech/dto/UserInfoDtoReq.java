package com.aptech.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class UserInfoDtoReq extends BaseDto {
	
	@NotEmpty
	private Long userId;
	
	@NotEmpty
	private String username;
	
	@NotEmpty
	private String firstName;

	@NotEmpty
	private String lastName;
	
	@Email
	private String email;
	
	private String story;

	public UserInfoDtoReq() {
		
	}
	
	public UserInfoDtoReq(Long userId, String username, String firstName, String lastName, String email, String avatarImg,
			String story) {
		super();
		this.userId = userId;
		this.username = username;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.story = story;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getStory() {
		return story;
	}

	public void setStory(String story) {
		this.story = story;
	}
}
