package com.aptech.dto.user;

import com.aptech.entity.AppRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppRoleDto {
	private Long id;

	private String name;

	public static AppRoleDto CreateFromEntity(AppRole src) {
		AppRoleDto dest = new AppRoleDto();

		dest.id = src.getId();
		dest.name = src.getName();

		return dest;
	}
}
