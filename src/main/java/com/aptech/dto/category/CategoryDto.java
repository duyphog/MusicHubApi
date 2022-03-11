package com.aptech.dto.category;

import com.aptech.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

	private Long id;

	private String name;

	private String description;

	public static CategoryDto CreateFromEntity(Category src) {
		CategoryDto dto = new CategoryDto();

		dto.id = src.getId();
		dto.name = src.getName();
		dto.description = src.getDescription();

		return dto;
	}
}
