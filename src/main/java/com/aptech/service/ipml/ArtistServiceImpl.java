package com.aptech.service.ipml;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.aptech.constant.AppError;
import com.aptech.domain.AppServiceResult;
import com.aptech.dto.album.AlbumShort;
import com.aptech.dto.artist.SingerInfoDto;
import com.aptech.dto.pagingation.PageDto;
import com.aptech.dto.pagingation.PageParam;
import com.aptech.dto.track.TrackShort;
import com.aptech.entity.Album;
import com.aptech.entity.Artist;
import com.aptech.entity.Track;
import com.aptech.repository.AlbumRepository;
import com.aptech.repository.ArtistRepository;
import com.aptech.repository.TrackRepository;
import com.aptech.service.ArtistService;

@Service
public class ArtistServiceImpl implements ArtistService {

	private final Logger logger = LoggerFactory.getLogger(ArtistServiceImpl.class);

	private ArtistRepository artistRepository;
	private AlbumRepository albumRepository;
	private TrackRepository trackRepository;

	@Autowired
	public ArtistServiceImpl(ArtistRepository artistRepository, AlbumRepository albumRepository,
			TrackRepository trackRepository) {
		this.artistRepository = artistRepository;
		this.albumRepository = albumRepository;
		this.trackRepository = trackRepository;
	}

	@Override
	public AppServiceResult<SingerInfoDto> getSingerInfo(Long singerId) {
		try {
			Artist singer = artistRepository.findSingerById(singerId);
			if (singer == null) {
				logger.warn("SingerId is not exist: " + singerId);

				return new AppServiceResult<SingerInfoDto>(false, AppError.Unknown.errorCode(),
						"SingerId is not exist: " + singerId, null);
			}

			SingerInfoDto info = SingerInfoDto.CreateFromEntity(singer);

			return new AppServiceResult<SingerInfoDto>(true, 0, "Succeed!", info);

		} catch (Exception e) {
			e.printStackTrace();

			return new AppServiceResult<SingerInfoDto>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}

	@Override
	public AppServiceResult<PageDto<AlbumShort>> searchAlbumBySingerIdWithPaging(Long singerId, PageParam pageParam) {
		try {
			Artist singer = artistRepository.findSingerById(singerId);
			if (singer == null) {
				logger.warn("SingerId is not exist: " + singerId);

				return new AppServiceResult<PageDto<AlbumShort>>(false, AppError.Unknown.errorCode(),
						"SingerId is not exist: " + singerId, null);
			}
			
			Page<Album> albums = albumRepository.findAllByIsActiveTrueAndSingersContaining(singer, pageParam.getPageable());
			Page<AlbumShort> dtoPage = albums.map(item -> AlbumShort.CreateFromEntity(item));
			
			return new AppServiceResult<PageDto<AlbumShort>>(true, 0, "Succeed!", new PageDto<AlbumShort>(dtoPage));

		} catch (Exception e) {
			e.printStackTrace();

			return new AppServiceResult<PageDto<AlbumShort>>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}

	@Override
	public AppServiceResult<PageDto<TrackShort>> searchTrackBySingerIdWithPaging(Long singerId, PageParam pageParam) {
		try {
			Artist singer = artistRepository.findSingerById(singerId);
			if (singer == null) {
				logger.warn("SingerId is not exist: " + singerId);

				return new AppServiceResult<PageDto<TrackShort>>(false, AppError.Unknown.errorCode(),
						"SingerId is not exist: " + singerId, null);
			}
			
			Page<Track> albums = trackRepository.findAllByIsActiveTrueAndSingersContaining(singer, pageParam.getPageable());
			Page<TrackShort> dtoPage = albums.map(item -> TrackShort.CreateFromEntity(item));
			
			return new AppServiceResult<PageDto<TrackShort>>(true, 0, "Succeed!", new PageDto<TrackShort>(dtoPage));

		} catch (Exception e) {
			e.printStackTrace();

			return new AppServiceResult<PageDto<TrackShort>>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}
}
