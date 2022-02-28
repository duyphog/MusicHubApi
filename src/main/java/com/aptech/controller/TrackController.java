package com.aptech.controller;

import java.util.ArrayList;
import java.util.List;

import com.aptech.dto.album.AlbumDto;
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
import com.aptech.dto.track.TrackCreate;
import com.aptech.dto.track.TrackDto;
import com.aptech.handle.exception.NotAnAudioFileException;
import com.aptech.service.ITrackService;

@RestController
@RequestMapping("/track")
public class TrackController {

	private ITrackService trackService;

	@Autowired
	public TrackController(ITrackService trackService) {
		this.trackService = trackService;
	}

//	@GetMapping
//	public ResponseEntity<HttpResponse> getAlbum() {
//
//		AppServiceResult<Iterable<GenreDto>> result = albumservice.getAlbum(1L);
//
//		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<Iterable<GenreDto>>(result.getData()))
//				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
//	}

	@GetMapping
	public ResponseEntity<HttpResponse> getTracks() {

		AppServiceResult<List<TrackDto>> result = trackService.getTracks();

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<List<TrackDto>>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}

	@PostMapping
	public ResponseEntity<HttpResponse> addTrack(@RequestParam(value = "name", required = true) String name,
			@RequestParam(value = "albumId", required = false) Long albumId,
			@RequestParam(value = "musicProduction", required = false) String musicProduction,
			@RequestParam(value = "musicYear", required = false) int musicYear,
			@RequestParam(value = "lyric", required = false) String lyric,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "categoryId", required = false) Long categoryId,
			@RequestParam(value = "singerIds", required = true) Long[] singerIds,
			@RequestParam(value = "composerIds", required = false, defaultValue = "") Long[] composerIds,
			@RequestParam(value = "genreIds", required = false) Long[] genreIds,
			@RequestParam(value = "trackFile", required = true) MultipartFile trackFile)
			throws NotAnAudioFileException {

		TrackCreate track = new TrackCreate(name, albumId, musicProduction, musicYear, lyric, description, categoryId,
				singerIds, composerIds, genreIds, trackFile);
		AppServiceResult<TrackDto> result = trackService.addTrack(track);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<TrackDto>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}

	@RequestMapping(path = "/{trackId}", method = RequestMethod.DELETE)
	public ResponseEntity<HttpResponse> removeTrack(@PathVariable("trackId") Long trackId) {

		AppBaseResult result = trackService.removeTrack(trackId);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<String>("Ok"))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}

	@RequestMapping(path = "/deactive/{trackId}", method = RequestMethod.PUT)
	public ResponseEntity<HttpResponse> deactiveTrack(@PathVariable("trackId") Long trackId) {

		AppBaseResult result = trackService.deactiveTrack(trackId);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<String>("Ok"))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}

	@RequestMapping(path = "/listened/{trackId}", method = RequestMethod.GET)
	public ResponseEntity<HttpResponse> listenedTrack(@PathVariable("trackId") Long trackId) {

		AppBaseResult result = trackService.listenedTrack(trackId);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<String>("Ok"))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}

	@RequestMapping(path = "/liked/{trackId}", method = RequestMethod.GET)
	public ResponseEntity<HttpResponse> likeTrack(@PathVariable("trackId") Long trackId) {

		AppBaseResult result = trackService.likedTrack(trackId, true);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<String>("Ok"))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}

	@RequestMapping(path = "/unliked/{trackId}", method = RequestMethod.GET)
	public ResponseEntity<HttpResponse> unLikeTrack(@PathVariable("trackId") Long trackId) {

		AppBaseResult result = trackService.likedTrack(trackId, false);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<String>("Ok"))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}

	@GetMapping(params = "statusid")
	public ResponseEntity<HttpResponse> getTrackByStatus(@RequestParam(value = "statusid") Long statusId) {

		AppServiceResult<List<TrackDto>> result = trackService.getTrackByAppStatus(statusId);

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<List<TrackDto>>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
}
