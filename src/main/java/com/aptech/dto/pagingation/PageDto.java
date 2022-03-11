package com.aptech.dto.pagingation;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageDto<T> {
	private List<T> content;
	private PageInfo pageInfo;

	public PageDto(Page<T> page) {
		if (page.hasContent()) {
			this.content = page.getContent();
		}

		this.pageInfo = new PageInfo();
		this.pageInfo.setCurrentPage(page.getNumber());
		this.pageInfo.setTotalPage(page.getTotalPages());
		this.pageInfo.setPageSize(page.getSize());
		this.pageInfo.setHasNext(page.hasNext());
		this.pageInfo.setHasPrevious(page.hasPrevious());
		this.pageInfo.setIsFirst(page.isFirst());
		this.pageInfo.setIsLast(page.isLast());
		this.pageInfo.setNumberOfElement(page.getNumberOfElements());
		this.pageInfo.setTotalElements(page.getTotalElements());
	}
}
