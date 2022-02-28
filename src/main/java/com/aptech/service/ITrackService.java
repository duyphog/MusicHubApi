 package com.aptech.service;

import java.util.List;

import com.aptech.domain.AppBaseResult;
import com.aptech.domain.AppServiceResult;
import com.aptech.dto.track.TrackCreate;
import com.aptech.dto.track.TrackDto;
import com.aptech.handle.exception.NotAnAudioFileException;

public interface ITrackService {

	AppServiceResult<List<TrackDto>> getTracks();
	
	AppServiceResult<TrackDto> getTrack();
	
	AppServiceResult<TrackDto> addTrack(TrackCreate track) throws NotAnAudioFileException;
	
	AppServiceResult<TrackDto> updateTrack();
	
	AppBaseResult removeTrack(Long trackId);
	
	AppBaseResult deactiveTrack(Long trackId);
	
	AppBaseResult likedTrack(Long trackId, boolean state);
	
	AppBaseResult listenedTrack(Long trackId);
	
	AppServiceResult<List<TrackDto>> getTrackByAppStatus(Long statusId);
	
	AppServiceResult<TrackDto> updateAppStatus(Long trackId, Long statusId);
}
