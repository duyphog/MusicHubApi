 package com.aptech.service;

import java.util.List;

import com.aptech.domain.AppBaseResult;
import com.aptech.domain.AppServiceResult;
import com.aptech.domain.FullTextSearchWithPagingParam;
import com.aptech.domain.SearchWithPagingParam;
import com.aptech.dto.pagingation.PageDto;
import com.aptech.dto.pagingation.PageParam;
import com.aptech.dto.track.TrackCreate;
import com.aptech.dto.track.TrackDto;
import com.aptech.dto.track.TrackShort;
import com.aptech.dto.user.UserWhiteList;
import com.aptech.provider.file.UnsupportedFileTypeException;

public interface TrackService {

	AppServiceResult<List<TrackDto>> getTracks();
	
	AppServiceResult<TrackDto> getTrack();
	
	AppServiceResult<TrackDto> getTrack(Long id);
	
	AppServiceResult<TrackDto> addTrack(TrackCreate track) throws UnsupportedFileTypeException;
	
	AppServiceResult<TrackDto> updateTrack();
	
	AppBaseResult removeTrack(Long trackId);
	
	AppBaseResult deactiveTrack(Long trackId);
	
	AppBaseResult likedTrack(Long trackId, boolean state);
	
	AppBaseResult listenedTrack(Long trackId);
	
	AppServiceResult<List<TrackDto>> getTrackByAppStatus(Long statusId);
	
	AppServiceResult<TrackDto> updateAppStatus(Long trackId, Long statusId);
	
	AppServiceResult<PageDto<TrackShort>> searchByCategoryAndGenre(SearchWithPagingParam params);
	
	AppServiceResult<PageDto<TrackShort>> searchByFTS(FullTextSearchWithPagingParam params);
	
	AppServiceResult<List<TrackShort>> getTopHitByCategory(Long categoryId);
	
	AppServiceResult<PageDto<TrackShort>> getAllTrackLiked(PageParam pageParam);
	
	AppServiceResult<Long[]> getTrackIdsLiked();
	
	AppBaseResult updateWhiteList(UserWhiteList dto);
}
