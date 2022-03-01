package com.aptech.service.ipml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aptech.constant.AppError;
import com.aptech.domain.AppBaseResult;
import com.aptech.domain.AppServiceResult;
import com.aptech.domain.MetaData;
import com.aptech.domain.SearchAlbumWithPagingParam;
import com.aptech.dto.album.AlbumCreate;
import com.aptech.dto.album.AlbumDto;
import com.aptech.dto.album.AlbumShort;
import com.aptech.dto.album.AlbumWithoutTrackDto;
import com.aptech.dto.pagingation.PageDto;
import com.aptech.entity.Album;
import com.aptech.entity.AppStatus;
import com.aptech.entity.AppUser;
import com.aptech.entity.Artist;
import com.aptech.entity.Category;
import com.aptech.entity.Genre;
import com.aptech.entity.Track;
import com.aptech.infrastructure.JaudiotaggerParser;
import com.aptech.provider.file.FileServiceFactory;
import com.aptech.provider.file.FileType;
import com.aptech.provider.file.FileService;
import com.aptech.provider.file.MediaFile;
import com.aptech.provider.file.UnsupportedFileTypeException;
import com.aptech.repository.AlbumRepository;
import com.aptech.repository.AppStatusRepository;
import com.aptech.repository.AppUserRepository;
import com.aptech.repository.ArtistRepository;
import com.aptech.repository.CategoryRepository;
import com.aptech.repository.GenreRepository;
import com.aptech.service.AlbumService;
import com.aptech.util.AppUtils;
import com.aptech.util.StringUtil;

@Service
public class AlbumServiceIpml implements AlbumService {

	private final Logger logger = LoggerFactory.getLogger(AlbumServiceIpml.class);

	private AppUserRepository appUserRepository;
	private AlbumRepository albumRepository;
	private CategoryRepository categoryRepository;
	private ArtistRepository artistRepository;
	private GenreRepository genreRepository;
	private AppStatusRepository appStatusRepository;

	private FileService imageFileService;
	private FileService trackFileService;

	@Autowired
	public AlbumServiceIpml(AppUserRepository appUserRepository, AlbumRepository albumRepository,
			CategoryRepository categoryRepository, ArtistRepository artistRepository, GenreRepository genreRepository,
			AppStatusRepository appStatusRepository) {
		this.appUserRepository = appUserRepository;
		this.albumRepository = albumRepository;
		this.categoryRepository = categoryRepository;
		this.artistRepository = artistRepository;
		this.genreRepository = genreRepository;
		this.appStatusRepository = appStatusRepository;

		this.imageFileService = FileServiceFactory.getFileService(FileType.IMAGE);
		this.trackFileService = FileServiceFactory.getFileService(FileType.TRACK);
	}

	@Override
	public AppServiceResult<List<AlbumDto>> getAlbums() {
		try {
			List<Album> entities = albumRepository.findAll();

			List<AlbumDto> result = new ArrayList<AlbumDto>();

			entities.forEach(item -> {
				result.add(AlbumDto.CreateFromEntity(item));
			});

			return new AppServiceResult<List<AlbumDto>>(true, 0, "Succeed!", result);

		} catch (Exception e) {
			e.printStackTrace();
			return new AppServiceResult<List<AlbumDto>>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}

	@Override
	public AppServiceResult<AlbumDto> getAlbum(Long id) {
		try {
			Album album = albumRepository.findById(id).orElse(null);

			if (album == null) {
				return new AppServiceResult<AlbumDto>(false, AppError.Validattion.errorCode(), id + " is not exist!",
						null);
			}

			AlbumDto dto = AlbumDto.CreateFromEntity(album);

			return new AppServiceResult<AlbumDto>(true, 0, "Success", dto);

		} catch (Exception e) {
			e.printStackTrace();
			return new AppServiceResult<AlbumDto>(false, AppError.Unknown.errorCode(), AppError.Unknown.errorMessage(),
					null);
		}
	}

	@Override
	public AppServiceResult<List<AlbumDto>> getAlbumForArtistId(Long id) {
		try {
			List<Album> albums = albumRepository.findAll();

			List<AlbumDto> result = new ArrayList<AlbumDto>();

			albums.forEach(item -> {
				result.add(AlbumDto.CreateFromEntity(item));
			});

			return new AppServiceResult<List<AlbumDto>>(true, 0, "Success", result);

		} catch (Exception e) {
			e.printStackTrace();
			return new AppServiceResult<List<AlbumDto>>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}

	@Override
	public AppServiceResult<AlbumDto> createAlbum(AlbumCreate album) throws UnsupportedFileTypeException {
		try {

			Album newAlbum = InitializeAlbumFromDto(album);

			albumRepository.save(newAlbum);

			final AlbumDto dto = AlbumDto.CreateFromEntity(newAlbum);

			return new AppServiceResult<AlbumDto>(true, 0, "Succeed!", dto);
		} catch (UnsupportedFileTypeException e) {
			e.printStackTrace();

			throw e;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();

			return new AppServiceResult<AlbumDto>(false, AppError.Validattion.errorCode(), e.getMessage(), null);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());

			return new AppServiceResult<AlbumDto>(false, AppError.Unknown.errorCode(), AppError.Unknown.errorMessage(),
					null);
		}
	}

	@Override
	public AppServiceResult<AlbumDto> createAlbumWithTracks(AlbumCreate album, MultipartFile[] files)
			throws UnsupportedFileTypeException {
		List<String> trackPaths = new ArrayList<String>();
		try {
			Album newAlbum = InitializeAlbumFromDto(album);
			trackPaths.add(newAlbum.getImgPath());

			for (MultipartFile trackFile : files) {
				Track track = AttachTrackFileInfomation(trackFile);

				trackPaths.add(track.getTrackPath());
				track.setCategory(newAlbum.getCategory());
				track.setAppUser(newAlbum.getAppUser());
				track.setUserNew(newAlbum.getUserNew());
				track.setAppStatus(newAlbum.getAppStatus());

				if (track.getSingers().isEmpty())
					track.setSingers(newAlbum.getSingers());

				if (track.getGenres().isEmpty())
					track.setGenres(newAlbum.getGenres());

				track.setAlbum(newAlbum);
				track.setIsActive(Boolean.FALSE);

				newAlbum.getTracks().add(track);
			}

			albumRepository.save(newAlbum);

			final AlbumDto dto = AlbumDto.CreateFromEntity(newAlbum);

			return new AppServiceResult<AlbumDto>(true, 0, "Succeed!", dto);
		} catch (UnsupportedFileTypeException e) {
			e.printStackTrace();

			this.removeListFile(trackPaths);

			throw e;
		} catch (IllegalArgumentException e) {
			e.printStackTrace();

			return new AppServiceResult<AlbumDto>(false, AppError.Validattion.errorCode(), e.getMessage(), null);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());

			this.removeListFile(trackPaths);
			return new AppServiceResult<AlbumDto>(false, AppError.Unknown.errorCode(), AppError.Unknown.errorMessage(),
					null);
		}
	}

	@Override
	public AppBaseResult deleteAlbum(Long id) {
		try {
			Album album = albumRepository.findById(id).orElse(null);
			if (album == null) {
				logger.warn("Album id is not exist: " + id + ". Can not handle farther!");
				return new AppServiceResult<AlbumDto>(false, AppError.Validattion.errorCode(),
						"Album id is not exist: " + id, null);
			} else {
				if (album.getTracks() != null)
					for (Track track : album.getTracks()) {
						track.setAlbum(null);
					}
				;

				albumRepository.delete(album);
				imageFileService.remove(album.getImgPath());
			}

			return AppBaseResult.GenarateIsSucceed();

		} catch (Exception e) {
			e.printStackTrace();

			return AppBaseResult.GenarateIsFailed(AppError.Unknown.errorCode(), AppError.Unknown.errorMessage());
		}
	}

	@Override
	public AppServiceResult<List<AlbumDto>> getAlbumByAppStatus(Long appStatusId) {
		try {
			List<Album> albums = albumRepository.findAllByAppStatusId(appStatusId);

			List<AlbumDto> result = new ArrayList<AlbumDto>();

			albums.forEach(item -> {
				result.add(AlbumDto.CreateFromEntity(item));
			});

			return new AppServiceResult<List<AlbumDto>>(true, 0, "Success", result);

		} catch (Exception e) {
			e.printStackTrace();

			return new AppServiceResult<List<AlbumDto>>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}

	@Override
	public AppServiceResult<AlbumWithoutTrackDto> updateAppStatus(Long albumId, Long appStatusId) {
		try {
			Album album = albumRepository.findById(albumId).orElse(null);
			if (album == null) {
				logger.warn("Album id is not exist: " + albumId + ". Can not handle farther!");
				return new AppServiceResult<AlbumWithoutTrackDto>(false, AppError.Validattion.errorCode(),
						"Album id is not exist: " + albumId, null);
			}

			AppStatus appStatus = appStatusRepository.findById(appStatusId).orElse(null);
			if (appStatus == null) {
				logger.warn("AppStatus id is not exist: " + appStatusId + ". Can not handle farther!");
				return new AppServiceResult<AlbumWithoutTrackDto>(false, AppError.Validattion.errorCode(),
						"AppStatus id is not exist: " + appStatusId, null);
			}

			album.setAppStatus(appStatus);
			album.setIsActive(appStatus.getSetActive());

			albumRepository.save(album);

			return new AppServiceResult<AlbumWithoutTrackDto>(true, 0, "Succeed!",
					AlbumWithoutTrackDto.CreateFromEntity(album));

		} catch (Exception e) {
			e.printStackTrace();

			return new AppServiceResult<AlbumWithoutTrackDto>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}

	@Override
	public AppServiceResult<PageDto<AlbumShort>> searchAlbumWithPaging(SearchAlbumWithPagingParam params) {
		try {
			Category category = categoryRepository.findById(params.getCategoryId()).orElse(null);
			
			Genre genre = params.getGenreId() == null ? null : genreRepository.findById(params.getGenreId()).orElse(null);
			
			if(category == null) {
				logger.warn("CategoryId is not exist: " + params.getCategoryId());
				
				return new AppServiceResult<PageDto<AlbumShort>>(false, AppError.Validattion.errorCode(),
						"CategoryId is not exist: " + params.getCategoryId(), null);
			}
			
			if(params.getGenreId() != null && genre == null) {
				logger.warn("GenreId is not exist: " + params.getGenreId());
				
				return new AppServiceResult<PageDto<AlbumShort>>(false, AppError.Validattion.errorCode(),
						"GenreId is not exist: " + params.getGenreId(), null);
			}
				
			Page<Album> results = params.getGenreId() == null
					? albumRepository.findAllByIsActiveTrueAndCategory(category, params.getPageParam().getPageable())
					: albumRepository.findAllByIsActiveTrueAndCategoryAndGenres(category, genre, params.getPageParam().getPageable());
		
			Page<AlbumShort> dtoPage = results.map(item -> AlbumShort.CreateFromEntity(item));
			
			return new AppServiceResult<PageDto<AlbumShort>>(true, 0, "Succeed!", new PageDto<AlbumShort>(dtoPage));
			
		} catch (Exception e) {
			e.printStackTrace();

			return new AppServiceResult<PageDto<AlbumShort>>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}

	private Album InitializeAlbumFromDto(AlbumCreate album)
			throws IOException, IllegalArgumentException, UnsupportedFileTypeException {
		Long countExistAlbumName = albumRepository.findContainsName(album.getName());

		if (countExistAlbumName > 0) {
			logger.warn("Album name is exist: " + album.getName() + ". Can not handle farther!");

			throw new IllegalArgumentException("Album name is exist " + album.getName());
		}

		Album newAlbum = new Album();

		newAlbum.setName(album.getName());
		newAlbum.setMusicProduction(album.getMusicProduction());
		newAlbum.setMusicYear(album.getMusicYear());
		newAlbum.getAppStatus();

		Category category = categoryRepository.findById(album.getCategoryId()).orElse(null);
		if (category == null) {
			logger.warn("Category id is not exist: " + album.getCategoryId() + ". Can not handle farther!");

			throw new IllegalArgumentException("Category id isnot exist: " + album.getCategoryId());
		}

		newAlbum.setCategory(category);
		newAlbum.setIsActive(Boolean.FALSE);

		for (Long singerId : album.getSingerIds()) {
			Artist singer = artistRepository.findSingerById(singerId);
			if (singer == null) {
				logger.warn("Singer id is not exist: " + singerId + ". Can not handle farther!");

				throw new IllegalArgumentException("Singer id is not exist: " + singerId);
			} else
				newAlbum.getSingers().add(singer);
		}

		for (Long genreId : album.getGenreIds()) {
			Genre genre = genreRepository.findById(genreId).orElse(null);
			if (genre == null) {
				logger.warn("Genre id is not exist: " + genreId + ". Can not handle farther!");

				throw new IllegalArgumentException("Genre id is not exist: " + genreId);
			} else
				newAlbum.getGenres().add(genre);
		}

		AppUser userLogedIn = appUserRepository.findByUsername(AppUtils.getCurrentUsername());
		newAlbum.setAppUser(userLogedIn);
		newAlbum.setUserNew(userLogedIn.getUsername());

		if (album.getImgFile() != null) {
			MediaFile mediaFile = imageFileService.upload(newAlbum.getName(), album.getImgFile());
			newAlbum.setImgUrl(mediaFile.getPathUrl());
			newAlbum.setImgPath(mediaFile.getPathFolder());
		}

		AppStatus defaultStatus = appStatusRepository.getDefaultAppStatus();
		newAlbum.setAppStatus(defaultStatus);

		return newAlbum;
	}

	private Track AttachTrackFileInfomation(MultipartFile trackFile) throws IOException, UnsupportedFileTypeException {
		Track track = new Track();

		MediaFile mediaFile = trackFileService
				.upload(StringUtil.getFileNameWithoutExtension(trackFile.getOriginalFilename()), trackFile);

		track.setTrackUrl(mediaFile.getPathUrl());
		track.setTrackPath(mediaFile.getPathFolder());

		MetaData metaData = JaudiotaggerParser.getRawMetaData(mediaFile.getFile());

		track.setName(metaData.getTitle());
		track.setMusicProduction(metaData.getProducer());
		track.setLyric(metaData.getLyric());
		track.setMusicYear(metaData.getYear());
		track.setLiked(0L);
		track.setListened(0L);

		TrySetComposerByName(track, metaData.getComposer());
		TrySetSingerByName(track, metaData.getArtist());
		TrySetGenreByName(track, metaData.getGenre());

		return track;
	}

	private void removeListFile(List<String> paths) {
		if (paths == null || paths.isEmpty())
			return;

		paths.forEach(path -> {
			trackFileService.remove(path);
		});
	}

	private void TrySetComposerByName(Track track, String composerName) {
		try {
			if (!StringUtil.isBlank(composerName)) {
				Artist composer = artistRepository.findComposerByNickName(composerName);
				if (composer != null)
					track.getComposers().add(composer);
			}
		} catch (Exception e) {
			// nothing
		}
	}

	private void TrySetSingerByName(Track track, String singerName) {
		try {
			if (!StringUtil.isBlank(singerName)) {
				Artist singer = artistRepository.findSingerByNickName(singerName);
				if (singer != null)
					track.getSingers().add(singer);
			}
		} catch (Exception e) {
			// nothing
		}
	}

	private void TrySetGenreByName(Track track, String genreName) {
		try {
			if (!StringUtil.isBlank(genreName)) {
				List<Genre> genres = genreRepository.findByName(genreName);

				if (genres != null)
					genres.forEach(genre -> {
						track.getGenres().add(genre);
					});
			}
		} catch (Exception e) {
			// nothing
		}
	}
}
