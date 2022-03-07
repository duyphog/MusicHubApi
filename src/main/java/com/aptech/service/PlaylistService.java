 package com.aptech.service;

import java.util.List;

import com.aptech.domain.AppBaseResult;
import com.aptech.domain.AppServiceResult;
import com.aptech.dto.pagingation.PageDto;
import com.aptech.dto.playlist.PlaylistCreate;
import com.aptech.dto.playlist.PlaylistDetailUpdate;
import com.aptech.dto.playlist.PlaylistDto;

public interface PlaylistService {
	
	AppServiceResult<List<PlaylistDto>> getPlaylists();
	AppServiceResult<PlaylistDto> getPlaylist(Long playlistId);
	AppServiceResult<PlaylistDto> createPlaylist(PlaylistCreate playlist);
	AppBaseResult removePlaylist(Long playlistId);
	
	AppBaseResult updateTrackToPlaylistDetail(PlaylistDetailUpdate dto);
	AppServiceResult<Iterable<PlaylistDto>> getPlaylistByAppUser(Long userId);

}
