package com.aptech.dto.appsatus;

import com.aptech.entity.AppStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppStatusDto {

	private Long id;

	private String name;
	private String description;

	public static AppStatusDto CreateFromEntity(AppStatus src) {
		AppStatusDto dest = new AppStatusDto();

		dest.id = src.getId();
		dest.name = src.getName();
		dest.description = src.getDescription();

		return dest;
	}
}
