package com.aptech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.aptech.domain.AppServiceResult;
import com.aptech.dto.HttpResponse;
import com.aptech.dto.HttpResponseError;
import com.aptech.dto.HttpResponseSuccess;
import com.aptech.dto.album.AlbumShort;
import com.aptech.dto.artist.SingerInfoDto;
import com.aptech.dto.pagingation.PageDto;
import com.aptech.dto.pagingation.PageParam;
import com.aptech.dto.track.TrackShort;
import com.aptech.service.ArtistService;

@RestController
@RequestMapping("/singer")
public class SingerController {
	
	private ArtistService artistService;
	
	@Autowired
	public SingerController(ArtistService artistService) {
		this.artistService = artistService;
	}
	
	@GetMapping(path = "/{id}")
	public ResponseEntity<HttpResponse> getInfo(@PathVariable(value = "id", required = true) Long singerId) {

		AppServiceResult<SingerInfoDto> result = artistService.getSingerInfo(singerId);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<SingerInfoDto>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
	
	@GetMapping(path = "/{id}/albums")
	public ResponseEntity<HttpResponse> getAlbumsBySinger(@PathVariable(value = "id", required = true) Long singerId,
			@RequestParam(name = "page-number", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(name = "page-size", required = false, defaultValue = "30") int pageSize) {

		PageParam pageParam = new PageParam();
		pageParam.setPageIndex(pageNumber);
		pageParam.setPageSize(pageSize);
		pageParam.setSortBy("dateNew");

		AppServiceResult<PageDto<AlbumShort>> result = artistService.searchAlbumBySingerIdWithPaging(singerId, pageParam);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<PageDto<AlbumShort>>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
	
	@GetMapping(path = "/{id}/tracks")
	public ResponseEntity<HttpResponse> getTracksBySinger(@PathVariable(value = "id", required = true) Long singerId,
			@RequestParam(name = "page-number", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(name = "page-size", required = false, defaultValue = "30") int pageSize) {

		PageParam pageParam = new PageParam();
		pageParam.setPageIndex(pageNumber);
		pageParam.setPageSize(pageSize);
		pageParam.setSortBy("dateNew");

		AppServiceResult<PageDto<TrackShort>> result = artistService.searchTrackBySingerIdWithPaging(singerId, pageParam);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<PageDto<TrackShort>>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
}
