package com.aptech.dto.appsatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAppStatus {

	private Long entityId;
	private Long appStatusId;
}
