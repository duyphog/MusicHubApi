package com.aptech.controller;

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
import com.aptech.dto.CreateTrack;
import com.aptech.dto.HttpResponse;
import com.aptech.dto.HttpResponseError;
import com.aptech.dto.HttpResponseSuccess;
import com.aptech.dto.TrackDto;
import com.aptech.handle.exception.NotAnAudioFileException;
import com.aptech.handle.exception.NotAnImageFileException;
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
	
	@PostMapping
	public ResponseEntity<HttpResponse> addTrack(
				@RequestParam(value="name") String name,
				@RequestParam(value="releaseDate", required = true) String releaseDate,
				@RequestParam(value="composerId", required = false) Long composerId,
				@RequestParam(value="genreId", required = false) Long genreId,
				@RequestParam(value="description") String description,
				@RequestParam(value="imageFile", required = false) MultipartFile imageFile,
				@RequestParam(value="trackFile", required = false) MultipartFile trackFile,
				@RequestParam(value="albumId", required = false) Long albumId,
				@RequestParam(value="artistId", required = false) Long artistId 
			) throws NotAnImageFileException, NotAnAudioFileException {
		
		CreateTrack track = new CreateTrack(name, releaseDate, composerId, genreId, description, imageFile, trackFile, albumId, artistId);
		AppServiceResult<TrackDto> result = trackService.addTrack(track);
		
		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<TrackDto>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
	
	@RequestMapping(path = "/remove/{trackId}", method=RequestMethod.POST)
	public ResponseEntity<HttpResponse> removeTrack(
			@PathVariable("filePath") long trackId
			) {
		
		AppBaseResult result = trackService.removeTrack(trackId);
		
		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<String>("Ok"))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
}
