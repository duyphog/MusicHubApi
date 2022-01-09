package com.aptech.service.ipml;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aptech.constant.AppError;
import com.aptech.domain.AppBaseResult;
import com.aptech.domain.AppServiceResult;
import com.aptech.dto.GenreDto;
import com.aptech.entity.Genre;
import com.aptech.entity.Track;
import com.aptech.repository.GenreRepository;
import com.aptech.repository.TrackRepository;
import com.aptech.service.IGenreService;
import com.aptech.service.ITrackService;


@Service
public class TrackService implements ITrackService {

	private final Logger logger = LoggerFactory.getLogger(TrackService.class);

	private TrackRepository trackRepository;

	@Autowired
	public TrackService(TrackRepository trackRepository) {
		this.trackRepository = trackRepository;
	}

	@Override
	public AppServiceResult<Track> getTrack() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AppServiceResult<Track> addTrack() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AppServiceResult<Track> updateTrack() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AppBaseResult removeTrack() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AppBaseResult likedTrack(boolean state) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AppBaseResult listenedTrack(Long trackId) {
		// TODO Auto-generated method stub
		return null;
	}

}
