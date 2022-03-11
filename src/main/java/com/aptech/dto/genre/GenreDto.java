package com.aptech.dto.genre;

import com.aptech.entity.Genre;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GenreDto {
	private Long id;

	private String name;

	private String description;

	public static GenreDto CreateFromEntity(Genre genre) {
		GenreDto dto = new GenreDto();

		dto.id = genre.getId();
		dto.name = genre.getName();
		dto.description = genre.getDescription();

		return dto;
	}
}
