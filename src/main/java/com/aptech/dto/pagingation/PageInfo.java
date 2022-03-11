package com.aptech.dto.pagingation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageInfo {
	private Long totalElements;
	private Integer numberOfElement;
	private Integer currentPage;
	private Integer pageSize;
	private Integer totalPage;
	private Boolean hasNext;
	private Boolean hasPrevious;
	private Boolean isFirst;
	private Boolean isLast;
}
