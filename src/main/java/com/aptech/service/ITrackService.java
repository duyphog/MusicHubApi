 package com.aptech.service;

import com.aptech.domain.AppBaseResult;
import com.aptech.domain.AppServiceResult;
import com.aptech.dto.CreateTrack;
import com.aptech.dto.TrackDto;
import com.aptech.handle.exception.NotAnAudioFileException;
import com.aptech.handle.exception.NotAnImageFileException;

public interface ITrackService {
	
	AppServiceResult<TrackDto> getTrack();
	
	AppServiceResult<TrackDto> addTrack(CreateTrack track) throws NotAnImageFileException, NotAnAudioFileException;
	
	AppServiceResult<TrackDto> updateTrack();
	
	AppBaseResult removeTrack(long trackId);
	
	AppBaseResult likedTrack(boolean state);
	
	AppBaseResult listenedTrack(Long trackId);
}
