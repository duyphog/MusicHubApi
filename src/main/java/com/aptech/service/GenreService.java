 package com.aptech.service;

import com.aptech.domain.AppServiceResult;
import com.aptech.dto.genre.GenreDto;

public interface GenreService {
	
	AppServiceResult<Iterable<GenreDto>> getGenres();
}
