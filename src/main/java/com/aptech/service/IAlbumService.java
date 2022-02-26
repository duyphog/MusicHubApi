 package com.aptech.service;

import java.util.List;

import com.aptech.domain.AppBaseResult;
import com.aptech.domain.AppServiceResult;
import com.aptech.dto.album.AlbumCreate;
import com.aptech.dto.album.AlbumDto;
import com.aptech.handle.exception.NotAnImageFileException;

public interface IAlbumService {
	
	AppServiceResult<List<AlbumDto>> getAlbums();
	
	AppServiceResult<AlbumDto> getAlbum(Long id);
	
	AppServiceResult<List<AlbumDto>> getAlbumForArtistId(Long userId);
	
	AppServiceResult<AlbumDto> createAlbum(AlbumCreate album) throws NotAnImageFileException;
	
	AppBaseResult deleteAlbum(Long id);
}
