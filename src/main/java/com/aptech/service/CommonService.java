package com.aptech.service;

import java.util.List;

import com.aptech.domain.AppServiceResult;
import com.aptech.dto.appsatus.AppStatusDto;
import com.aptech.dto.artist.ComposerDto;
import com.aptech.dto.artist.SingerDto;
import com.aptech.dto.category.CategoryDto;
import com.aptech.dto.genre.GenreDto;
import com.aptech.dto.playlist.PlaylistTypeDto;

public interface CommonService {
	AppServiceResult<List<SingerDto>> searchSinger(String searchString);

	AppServiceResult<List<ComposerDto>> searchComposer(String searchString);

	AppServiceResult<List<CategoryDto>> getCategories();

	AppServiceResult<List<GenreDto>> getGenres();
	
	AppServiceResult<List<AppStatusDto>> getAppStatus();
	
	AppServiceResult<List<PlaylistTypeDto>> getPlaylistTypes();
}
