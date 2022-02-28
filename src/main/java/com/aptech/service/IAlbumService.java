package com.aptech.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.aptech.domain.AppBaseResult;
import com.aptech.domain.AppServiceResult;
import com.aptech.dto.album.AlbumCreate;
import com.aptech.dto.album.AlbumDto;
import com.aptech.dto.album.AlbumWithoutTrackDto;
import com.aptech.provider.file.UnsupportedFileTypeException;

public interface IAlbumService {

	AppServiceResult<List<AlbumDto>> getAlbums();

	AppServiceResult<AlbumDto> getAlbum(Long id);

	AppServiceResult<List<AlbumDto>> getAlbumForArtistId(Long userId);

	AppServiceResult<AlbumDto> createAlbum(AlbumCreate album) throws UnsupportedFileTypeException;

	AppServiceResult<AlbumDto> createAlbumWithTracks(AlbumCreate album, MultipartFile[] files)
			throws UnsupportedFileTypeException;

	AppBaseResult deleteAlbum(Long id);

	AppServiceResult<List<AlbumDto>> getAlbumByAppStatus(Long statusId);

	AppServiceResult<AlbumWithoutTrackDto> updateAppStatus(Long albumId, Long appStatusId);
}
