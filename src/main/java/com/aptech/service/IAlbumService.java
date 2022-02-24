 package com.aptech.service;

import java.util.List;

import com.aptech.domain.AppServiceResult;
import com.aptech.dto.album.AlbumDto;
import com.aptech.dto.album.AlbumRes;
import com.aptech.handle.exception.NotAnImageFileException;

public interface IAlbumService {
	
	AppServiceResult<AlbumRes> getAlbum(Long id);
	
	AppServiceResult<List<AlbumRes>> getAlbumForUserId(Long userId);
	
	AppServiceResult<AlbumRes> addAlbum(AlbumDto albumDto) throws NotAnImageFileException;
	
	AppServiceResult<AlbumRes> deleteAlbum(Long id);
}
