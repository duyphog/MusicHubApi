package com.aptech.dto.pagingation;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.aptech.util.StringUtil;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageParam {
	private Integer pageIndex = 0;
	private Integer pageSize = 30;
	private String sortBy = null;
	private Boolean isAcsending = true;

	public Boolean isSort() {
		return (sortBy == null || StringUtil.isBlank(sortBy)) ? false : true;
	}

	public Sort getSort() {
		if (sortBy != null || !StringUtil.isBlank(sortBy))
			return isAcsending ? Sort.by(sortBy) : Sort.by(sortBy).descending();
		else
			return null;
	}

	public Pageable getPageable() {
		if (sortBy == null || StringUtil.isBlank(sortBy))
			return PageRequest.of(this.pageIndex, this.pageSize);
		else
			return PageRequest.of(this.pageIndex, this.pageSize,
					isAcsending ? Sort.by(sortBy) : Sort.by(sortBy).descending());
	}

}
