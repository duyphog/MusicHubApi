package com.aptech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.aptech.domain.AppServiceResult;
import com.aptech.dto.AlbumDto;
import com.aptech.dto.AlbumRes;
import com.aptech.dto.HttpResponse;
import com.aptech.dto.HttpResponseError;
import com.aptech.dto.HttpResponseSuccess;
import com.aptech.handle.exception.NotAnImageFileException;
import com.aptech.service.IAlbumService;
import com.aptech.util.AppUtils;

@RestController
@RequestMapping("/album")
public class AlbumController {
	
	private IAlbumService albumservice;
	
	@Autowired
	public AlbumController(IAlbumService albumservice) {
		this.albumservice = albumservice;
	}
	
//	@GetMapping
//	public ResponseEntity<HttpResponse> getAlbum() {
//
//		AppServiceResult<Iterable<GenreDto>> result = albumservice.getAlbum(1L);
//
//		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<Iterable<GenreDto>>(result.getData()))
//				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
//	}
	
	@PostMapping
	public ResponseEntity<HttpResponse> addAlbum(
			@RequestParam(value="name") String name,
			@RequestParam(value="description") String description,
			@RequestParam(value="releaseDate", required = true) String releaseDate,
			@RequestParam(value="imageFile", required = false) MultipartFile imageFile,
			@RequestParam(value="artistId", required = false) Long artistId ) throws NotAnImageFileException {
		
		AlbumDto dto = new AlbumDto(name, description, AppUtils.ParseDateString(releaseDate), imageFile, artistId);
		
		AppServiceResult<AlbumRes> result = albumservice.addAlbum(dto);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<AlbumRes>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
}
