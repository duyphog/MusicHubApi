package com.aptech.dto.user;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserStatus {

	@NotNull
	private Long userId;

	@NotNull
	private Boolean isActive;

}
