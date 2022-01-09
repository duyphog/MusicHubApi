 package com.aptech.service;

import com.aptech.domain.AppServiceResult;
import com.aptech.entity.Album;

public interface IAlbumService {
	
	AppServiceResult<Album> getAlbum();
}
