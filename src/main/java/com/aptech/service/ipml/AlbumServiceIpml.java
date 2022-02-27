package com.aptech.service.ipml;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.aptech.constant.AppError;
import com.aptech.constant.FileConstant;
import com.aptech.domain.AppBaseResult;
import com.aptech.domain.AppServiceResult;
import com.aptech.domain.MetaData;
import com.aptech.dto.album.AlbumCreate;
import com.aptech.dto.album.AlbumDto;
import com.aptech.dto.album.AlbumWithoutTrackDto;
import com.aptech.entity.Album;
import com.aptech.entity.AppStatus;
import com.aptech.entity.AppUser;
import com.aptech.entity.Artist;
import com.aptech.entity.Category;
import com.aptech.entity.Genre;
import com.aptech.entity.Track;
import com.aptech.handle.exception.NotAnAudioFileException;
import com.aptech.handle.exception.NotAnImageFileException;
import com.aptech.infrastructure.JaudiotaggerParser;
import com.aptech.provider.FileManager;
import com.aptech.provider.file.FileServiceFactory;
import com.aptech.provider.file.FileType;
import com.aptech.provider.file.IFileService;
import com.aptech.provider.file.MediaFile;
import com.aptech.provider.file.UnsupportedFileTypeException;
import com.aptech.repository.AlbumRepository;
import com.aptech.repository.AppStatusRepository;
import com.aptech.repository.AppUserRepository;
import com.aptech.repository.ArtistRepository;
import com.aptech.repository.CategoryRepository;
import com.aptech.repository.GenreRepository;
import com.aptech.service.IAlbumService;
import com.aptech.util.AppUtils;
import com.aptech.util.FileUtil;
import com.aptech.util.StringUtil;

@Service
public class AlbumServiceIpml implements IAlbumService {

	private final Logger logger = LoggerFactory.getLogger(AlbumServiceIpml.class);

	private AppUserRepository appUserRepository;
	private AlbumRepository albumRepository;
	private CategoryRepository categoryRepository;
	private ArtistRepository artistRepository;
	private GenreRepository genreRepository;
	private AppStatusRepository appStatusRepository;

	private FileManager fileManager;
	private IFileService imageFileService;
	private IFileService trackFileService;

	@Autowired
	public AlbumServiceIpml(AppUserRepository appUserRepository, AlbumRepository albumRepository,
			CategoryRepository categoryRepository, ArtistRepository artistRepository, GenreRepository genreRepository,
			AppStatusRepository appStatusRepository, FileManager fileManager) {
		this.appUserRepository = appUserRepository;
		this.albumRepository = albumRepository;
		this.categoryRepository = categoryRepository;
		this.artistRepository = artistRepository;
		this.genreRepository = genreRepository;
		this.appStatusRepository = appStatusRepository;
		this.fileManager = fileManager;
		
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
	public AppServiceResult<AlbumDto> createAlbum(AlbumCreate album) throws NotAnImageFileException {
		try {

			Album newAlbum = InitializeAlbumFromDto(album);

			albumRepository.save(newAlbum);

			final AlbumDto dto = AlbumDto.CreateFromEntity(newAlbum);

			return new AppServiceResult<AlbumDto>(true, 0, "Succeed!", dto);
		} catch (NotAnImageFileException e) {
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
			throws NotAnImageFileException, NotAnAudioFileException {
		try {

			Album newAlbum = InitializeAlbumFromDto(album);

			for (MultipartFile trackFile : files) {
				Track track = AttachTrackFileInfomation(trackFile);
				
//				track.setMusicProduction(newAlbum.getMusicProduction());
//				track.setMusicYear(newAlbum.getMusicYear());
				track.setCategory(newAlbum.getCategory());
				track.setAppUser(newAlbum.getAppUser());
				track.setUserNew(newAlbum.getUserNew());
				track.setAppStatus(newAlbum.getAppStatus());
				track.setSingers(newAlbum.getSingers());
				track.setGenre(newAlbum.getGenres());
				
				track.setAlbum(newAlbum);
				track.setIsActive(Boolean.FALSE);
				
				newAlbum.getTracks().add(track);
			}
			
			albumRepository.save(newAlbum);

			final AlbumDto dto = AlbumDto.CreateFromEntity(newAlbum);

			return new AppServiceResult<AlbumDto>(true, 0, "Succeed!", dto);
		} catch (NotAnImageFileException e) {
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
	public AppBaseResult deleteAlbum(Long id) {
		try {
			Album album = albumRepository.findById(id).orElse(null);
			if (album == null) {
				logger.warn("Album id is not exist: " + id + ". Can not handle farther!");
				return new AppServiceResult<AlbumDto>(false, AppError.Validattion.errorCode(),
						"Album id is not exist: " + id, null);
			} else {
				if(album.getTracks() != null)
					for (Track track : album.getTracks()) {
						track.setAlbum(null);
					};
				
				albumRepository.delete(album);
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

	private Album InitializeAlbumFromDto(AlbumCreate album)
			throws IOException, NotAnImageFileException, IllegalArgumentException, UnsupportedFileTypeException {
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
	
	private Track AttachTrackFileInfomation(MultipartFile trackFile) throws IOException, NotAnAudioFileException {
		Track track = new Track();
		
		MetaData metaData =  JaudiotaggerParser.getRawMetaData(FileUtil.convert(trackFile));
		
		track.setName(metaData.getTitle());
		track.setLyric(metaData.getLyric());
		track.setMusicYear(metaData.getYear());
		track.setLiked(0L);
		track.setListened(0L);
		
		Path songFolder = Paths.get(FileConstant.TRACK_FOLDER).toAbsolutePath().normalize();

		String trackUrl = fileManager.uploadAudioFile(songFolder, AppUtils.getCurrentUsername(),
				StringUtil.normalizeUri(track.getName()), trackFile);
		
		track.setTrackUrl(trackUrl);
		
		return track;
	}
}
