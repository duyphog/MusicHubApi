package com.aptech.service;

import com.aptech.domain.AppServiceResult;
import com.aptech.dto.album.AlbumShort;
import com.aptech.dto.artist.SingerInfoDto;
import com.aptech.dto.pagingation.PageDto;
import com.aptech.dto.pagingation.PageParam;
import com.aptech.dto.track.TrackShort;

public interface ArtistService {

	AppServiceResult<SingerInfoDto> getSingerInfo(Long singerId);
	
	AppServiceResult<PageDto<AlbumShort>> searchAlbumBySingerIdWithPaging(Long singerId, PageParam pageParam);
	
	AppServiceResult<PageDto<TrackShort>> searchTrackBySingerIdWithPaging(Long singerId, PageParam pageParam);
}
