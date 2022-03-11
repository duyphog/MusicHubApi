package com.aptech.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aptech.domain.AppBaseResult;
import com.aptech.domain.AppServiceResult;
import com.aptech.domain.SearchWithPagingParam;
import com.aptech.dto.HttpResponse;
import com.aptech.dto.HttpResponseError;
import com.aptech.dto.HttpResponseSuccess;
import com.aptech.dto.album.AlbumCreate;
import com.aptech.dto.album.AlbumDto;
import com.aptech.dto.album.AlbumForAdminDto;
import com.aptech.dto.album.AlbumShort;
import com.aptech.dto.pagingation.PageDto;
import com.aptech.provider.file.UnsupportedFileTypeException;
import com.aptech.service.AlbumService;

@RestController
@RequestMapping("/album")
public class AlbumController {

	private AlbumService albumservice;

	@Autowired
	public AlbumController(AlbumService albumservice) {
		this.albumservice = albumservice;
	}

	@GetMapping(path = "/admin/list")
	public ResponseEntity<HttpResponse> getAlbumForAdminPage() {

		AppServiceResult<List<AlbumForAdminDto>> result = albumservice.getAlbums();

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<List<AlbumForAdminDto>>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
	
	@GetMapping(path = "/search")
	public ResponseEntity<HttpResponse> getAlbumsByParam(@RequestParam(name = "category-id") long categoryId,
			@RequestParam(name = "genre-id", required = false, defaultValue = "0") long genreId,
			@RequestParam(name = "page-number", required = false, defaultValue = "0") int pageNumber,
			@RequestParam(name = "page-size", required = false, defaultValue = "30") int pageSize) {

		SearchWithPagingParam params = new SearchWithPagingParam();
		params.setCategoryId(categoryId);
		params.setGenreId(genreId == 0 ? null : genreId);
		params.getPageParam().setPageIndex(pageNumber);
		params.getPageParam().setPageSize(pageSize);
		params.getPageParam().setSortBy("dateNew");

		AppServiceResult<PageDto<AlbumShort>> result = albumservice.searchAlbumWithPaging(params);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<PageDto<AlbumShort>>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}

	@GetMapping(params = "statusid")
	public ResponseEntity<HttpResponse> getAlbumByStatus(@RequestParam(name = "statusid") Long statusId) {

		AppServiceResult<List<AlbumDto>> result = albumservice.getAlbumByAppStatus(statusId);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<List<AlbumDto>>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<HttpResponse> getAlbum(@PathVariable("id") Long id) {

		AppServiceResult<AlbumDto> result = albumservice.getAlbum(id);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<AlbumDto>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}

	@PostMapping
	public ResponseEntity<HttpResponse> createAlbum(@RequestParam(value = "name") String name,
			@RequestParam(value = "musicProduction") String musicProduction,
			@RequestParam(value = "musicYear", required = true) int musicYear,
			@RequestParam(value = "imgFile", required = false) MultipartFile imgFile,
			@RequestParam(value = "categoryId", required = true) Long categoryId,
			@RequestParam(value = "singerIds", required = true) Long[] singerIds,
			@RequestParam(value = "genreIds", required = true) Long[] genreIds,
			@RequestParam(value = "trackFiles", required = false) MultipartFile[] trackFiles)
			throws UnsupportedFileTypeException {

		AlbumCreate newAlbum = new AlbumCreate(name, musicProduction, musicYear, imgFile, categoryId, singerIds,
				genreIds);

		AppServiceResult<AlbumDto> result = trackFiles == null ? albumservice.createAlbum(newAlbum)
				: albumservice.createAlbumWithTracks(newAlbum, trackFiles);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<AlbumDto>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<HttpResponse> deleteAlbum(@PathVariable("id") Long id) {

		AppBaseResult result = albumservice.deleteAlbum(id);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<String>("Deleted!"))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
}
