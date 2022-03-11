 package com.aptech.service;

import java.util.List;

import com.aptech.domain.AppBaseResult;
import com.aptech.domain.AppServiceResult;
import com.aptech.dto.pagingation.PageDto;
import com.aptech.dto.pagingation.PageParam;
import com.aptech.dto.playlist.PlaylistCreate;
import com.aptech.dto.playlist.PlaylistDetailUpdate;
import com.aptech.dto.playlist.PlaylistDto;
import com.aptech.dto.playlist.PlaylistForAdminDto;
import com.aptech.dto.playlist.PlaylistShort;

public interface PlaylistService {
	
	AppServiceResult<List<PlaylistForAdminDto>> getPlaylists();
	
	AppServiceResult<PlaylistDto> getPlaylist(Long playlistId);
	
	AppServiceResult<PlaylistDto> createPlaylist(PlaylistCreate playlist);
	
	AppBaseResult removePlaylist(Long playlistId);
	
	AppBaseResult updateTrackToPlaylistDetail(PlaylistDetailUpdate dto);
	
	AppServiceResult<List<PlaylistShort>> getPlaylistByType(Long playlistTypeId);
	
	AppServiceResult<PageDto<PlaylistShort>> getPlaylistByUserLoggedIn(PageParam pageParam);
}
