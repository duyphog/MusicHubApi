package com.aptech.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import com.aptech.dto.playlist.PlaylistCreate;
import com.aptech.dto.playlist.PlaylistDetailUpdate;
import com.aptech.dto.playlist.PlaylistDto;
import com.aptech.provider.file.UnsupportedFileTypeException;
import com.aptech.service.PlaylistService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/playlist")
public class PlaylistController {

	private PlaylistService playlistService;

	@Autowired
	public PlaylistController(PlaylistService playlistService) {
		this.playlistService = playlistService;
	}

	@RequestMapping(path = "/single/{playlistId}", method = RequestMethod.GET)
	public ResponseEntity<HttpResponse> getTrack(@PathVariable(value = "playlistId", required = true) Long playlistId) {

		AppServiceResult<PlaylistDto> result = playlistService.getPlaylist(playlistId);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<PlaylistDto>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}

	@PostMapping
	public ResponseEntity<HttpResponse> createPlaylist(@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "playlistTypeId", required = false) Long playlistTypeId,
			@RequestParam(value = "categoryId", required = false) Long categoryId,
			@RequestParam(value = "genreId", required = false) Long genreId,
			@RequestParam(value = "imgFile", required = false) MultipartFile imgFile)
			throws UnsupportedFileTypeException {

		PlaylistCreate playlist = new PlaylistCreate(name, description, categoryId, genreId, playlistTypeId, imgFile);
		AppServiceResult<PlaylistDto> result = playlistService.createPlaylist(playlist);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<PlaylistDto>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}

	@PostMapping(path = "/details/update")
	public ResponseEntity<HttpResponse> updateTrackToDetails(@Valid @RequestBody PlaylistDetailUpdate dto) {

		AppBaseResult result = playlistService.updateTrackToPlaylistDetail(dto);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<String>("Succeed!"))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<HttpResponse> removePlaylist(@PathVariable("id") Long id) {
		AppBaseResult result = playlistService.removePlaylist(id);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<String>("Succeed!"))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
}
