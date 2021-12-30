package com.aptech.dto;

public class UserLoginRes {
	private Long userId;
	private String username;
	private String token;

	public UserLoginRes() {
	}

	public UserLoginRes(Long userId, String username, String token) {
		super();
		this.userId = userId;
		this.username = username;
		this.token = token;
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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

}
