package com.aptech.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.aptech.domain.AppBaseResult;
import com.aptech.domain.AppServiceResult;
import com.aptech.dto.HttpResponse;
import com.aptech.dto.HttpResponseError;
import com.aptech.dto.HttpResponseSuccess;
import com.aptech.dto.playlist.PlaylistCreate;
import com.aptech.dto.playlist.PlaylistDetailUpdate;
import com.aptech.dto.playlist.PlaylistDto;
import com.aptech.dto.playlist.PlaylistShort;
import com.aptech.dto.track.TrackShort;
import com.aptech.provider.file.UnsupportedFileTypeException;
import com.aptech.service.PlaylistService;

@RestController
@RequestMapping("/playlist")
public class PlaylistController {

	private PlaylistService playlistService;

	@Autowired
	public PlaylistController(PlaylistService playlistService) {
		this.playlistService = playlistService;
	}

	@GetMapping
	public ResponseEntity<HttpResponse> getPlaylists() {

		AppServiceResult<List<PlaylistDto>> result = playlistService.getPlaylists();

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<Iterable<PlaylistDto>>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
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

	@PostMapping("/details")
	public ResponseEntity<HttpResponse> updateTrackToDetails(@Valid @RequestBody PlaylistDetailUpdate update) {

		AppBaseResult result = playlistService.updateTrackToPlaylistDetail(update);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<String>("Succeed!"))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}

	@RequestMapping(path = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<HttpResponse> removePlaylist(@PathVariable("id") Long id) {
		AppBaseResult result = playlistService.removePlaylist(id);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<String>("Succeed!"))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
	
	@GetMapping(path = "/category")
	public ResponseEntity<HttpResponse> getPlaylistByType(
				@RequestParam(value = "playlistTypeId", required = true) Long playlistTypeId) {
			
		AppServiceResult<List<PlaylistShort>> result = playlistService.getPlaylistByType(playlistTypeId);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<List<PlaylistShort>>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
}
