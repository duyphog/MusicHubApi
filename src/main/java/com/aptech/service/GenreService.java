 package com.aptech.service;

import com.aptech.domain.AppServiceResult;
import com.aptech.dto.genre.GenreDto;

import java.util.List;

 public interface GenreService {
	
	AppServiceResult<List<GenreDto>> getGenres();
}
