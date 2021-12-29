package com.aptech.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AppUserDto extends BaseDto {
	private Long userId;
	private String username;
	private String email;
	private String token;
	private String firstName;
	private String lastName;
	private String avatarImg;
	private String story;
	
}
