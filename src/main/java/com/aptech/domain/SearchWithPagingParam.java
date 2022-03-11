package com.aptech.domain;

import com.aptech.dto.pagingation.PageParam;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchWithPagingParam {
	private Long categoryId;
	private Long genreId;
	private PageParam pageParam = new PageParam();
}
