package com.aptech.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.aptech.domain.AppServiceResult;
import com.aptech.dto.HttpResponse;
import com.aptech.dto.HttpResponseError;
import com.aptech.dto.HttpResponseSuccess;
import com.aptech.dto.appsatus.AppStatusDto;
import com.aptech.dto.artist.ComposerDto;
import com.aptech.dto.artist.SingerDto;
import com.aptech.dto.category.CategoryDto;
import com.aptech.dto.genre.GenreDto;
import com.aptech.dto.playlist.PlaylistTypeDto;
import com.aptech.service.CommonService;

@RestController
@RequestMapping("/common")
public class CommonController {

	private CommonService commonService;

	@Autowired
	public CommonController(CommonService commonService) {
		this.commonService = commonService;
	}

	@GetMapping(path = "/search-singer")
	public ResponseEntity<HttpResponse> searchSinger(@RequestParam String search) {

		AppServiceResult<List<SingerDto>> result = commonService.searchSinger(search);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<List<SingerDto>>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}

	@GetMapping(path = "/search-composer")
	public ResponseEntity<HttpResponse> searchComposer(@RequestParam String search) {

		AppServiceResult<List<ComposerDto>> result = commonService.searchComposer(search);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<List<ComposerDto>>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}

	@GetMapping(path = "/genre")
	public ResponseEntity<HttpResponse> getGenres() {

		AppServiceResult<List<GenreDto>> result = commonService.getGenres();

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<List<GenreDto>>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}

	@GetMapping(path = "/category")
	public ResponseEntity<HttpResponse> getCategory() {

		AppServiceResult<List<CategoryDto>> result = commonService.getCategories();

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<List<CategoryDto>>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
	
	@GetMapping(path = "/appstatus")
	public ResponseEntity<HttpResponse> getAppStatus() {

		AppServiceResult<List<AppStatusDto>> result = commonService.getAppStatus();

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<List<AppStatusDto>>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
	
	@GetMapping(path = "/playlist-type")
	public ResponseEntity<HttpResponse> getPlaylistType() {

		AppServiceResult<List<PlaylistTypeDto>> result = commonService.getPlaylistTypes();

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<List<PlaylistTypeDto>>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
}
