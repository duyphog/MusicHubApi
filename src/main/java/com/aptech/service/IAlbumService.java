 package com.aptech.service;

import java.util.List;

import com.aptech.domain.AppServiceResult;
import com.aptech.dto.AlbumDto;
import com.aptech.entity.Album;
import com.aptech.handle.exception.NotAnImageFileException;

public interface IAlbumService {
	
	AppServiceResult<Album> getAlbum(Long id);
	
	AppServiceResult<List<Album>> getAlbumForUserId(Long userId);
	
	AppServiceResult<Album> addAlbum(AlbumDto albumDto) throws NotAnImageFileException;
	
	AppServiceResult<Album> deleteAlbum(Long id);
}
