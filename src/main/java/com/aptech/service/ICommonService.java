package com.aptech.service;

import java.util.List;

import com.aptech.domain.AppServiceResult;
import com.aptech.dto.artist.ComposerDto;
import com.aptech.dto.artist.SingerDto;
import com.aptech.dto.category.CategoryDto;
import com.aptech.dto.genre.GenreDto;

public interface ICommonService {
	AppServiceResult<List<SingerDto>> SearchSinger(String searchString);

	AppServiceResult<List<ComposerDto>> SearchComposer(String searchString);

	AppServiceResult<List<CategoryDto>> GetCategories();

	AppServiceResult<List<GenreDto>> GetGenres();
}
