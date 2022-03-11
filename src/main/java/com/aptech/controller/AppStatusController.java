package com.aptech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.aptech.domain.AppServiceResult;
import com.aptech.dto.HttpResponse;
import com.aptech.dto.HttpResponseError;
import com.aptech.dto.HttpResponseSuccess;
import com.aptech.dto.album.AlbumWithoutTrackDto;
import com.aptech.dto.appsatus.UpdateAppStatus;
import com.aptech.dto.track.TrackDto;
import com.aptech.service.AlbumService;
import com.aptech.service.TrackService;

import io.swagger.v3.oas.annotations.parameters.RequestBody;

@RestController
@RequestMapping("/app-status")
public class AppStatusController {

	private AlbumService albumservice;
	private TrackService trackService;

	@Autowired
	public AppStatusController(AlbumService albumservice, TrackService trackService) {
		this.albumservice = albumservice;
		this.trackService = trackService;
	}

	@RequestMapping(path = "/album", method = RequestMethod.PUT)
	public ResponseEntity<HttpResponse> updateAlbumStatus(@RequestBody UpdateAppStatus updateAppStatus) {

		AppServiceResult<AlbumWithoutTrackDto> result = albumservice.updateAppStatus(updateAppStatus.getEntityId(),
				updateAppStatus.getAppStatusId());

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<AlbumWithoutTrackDto>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}

	@RequestMapping(path = "/track", method = RequestMethod.PUT)
	public ResponseEntity<HttpResponse> updateTrackStatus(@RequestBody UpdateAppStatus updateAppStatus) {

		AppServiceResult<TrackDto> result = trackService.updateAppStatus(updateAppStatus.getEntityId(),
				updateAppStatus.getAppStatusId());

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<TrackDto>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
}
