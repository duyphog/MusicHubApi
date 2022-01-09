 package com.aptech.service;

import com.aptech.domain.AppBaseResult;
import com.aptech.domain.AppServiceResult;
import com.aptech.entity.Track;

public interface ITrackService {
	
	AppServiceResult<Track> getTrack();
	
	AppServiceResult<Track> addTrack();
	
	AppServiceResult<Track> updateTrack();
	
	AppBaseResult removeTrack();
	
	AppBaseResult likedTrack(boolean state);
	
	AppBaseResult listenedTrack(Long trackId);
}
