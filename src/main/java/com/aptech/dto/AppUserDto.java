package com.aptech.dto;

public class AppUserDto extends BaseDto {
	private Long userId;
	private String username;
	private String email;
	private String token;
	private String firstName;
	private String lastName;
	private String avatarImg;
	private String story;
	
	public AppUserDto() {
		
	}
			
	public AppUserDto(Long userId, String username, String email, String token, String firstName, String lastName,
			String avatarImg, String story) {
		super();
		this.userId = userId;
		this.username = username;
		this.email = email;
		this.token = token;
		this.firstName = firstName;
		this.lastName = lastName;
		this.avatarImg = avatarImg;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
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

	public String getAvatarImg() {
		return avatarImg;
	}

	public void setAvatarImg(String avatarImg) {
		this.avatarImg = avatarImg;
	}

	public String getStory() {
		return story;
	}

	public void setStory(String story) {
		this.story = story;
	}
}
