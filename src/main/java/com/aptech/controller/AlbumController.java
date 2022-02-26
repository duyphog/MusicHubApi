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
import com.aptech.dto.HttpResponse;
import com.aptech.dto.HttpResponseError;
import com.aptech.dto.HttpResponseSuccess;
import com.aptech.dto.album.AlbumCreate;
import com.aptech.dto.album.AlbumDto;
import com.aptech.handle.exception.NotAnAudioFileException;
import com.aptech.handle.exception.NotAnImageFileException;
import com.aptech.service.IAlbumService;

@RestController
@RequestMapping("/album")
public class AlbumController {

	private IAlbumService albumservice;

	@Autowired
	public AlbumController(IAlbumService albumservice) {
		this.albumservice = albumservice;
	}

	@GetMapping
	public ResponseEntity<HttpResponse> getAlbums() {

		AppServiceResult<List<AlbumDto>> result = albumservice.getAlbums();

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<List<AlbumDto>>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}

	@GetMapping(params = "statusid")
	public ResponseEntity<HttpResponse> getAlbumByStatus(@RequestParam(value = "statusid") Long statusId) {

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
			throws NotAnImageFileException, NotAnAudioFileException {

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
