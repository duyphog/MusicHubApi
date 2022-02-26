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
public class UserWhiteList {

	@NotNull
	private Long trackId;

	@NotNull
	private Boolean isAdd; // false -> remove
}
