package com.aptech.service.ipml;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aptech.constant.AppError;
import com.aptech.constant.FileConstant;
import com.aptech.domain.AppBaseResult;
import com.aptech.domain.AppServiceResult;
import com.aptech.dto.album.AlbumCreate;
import com.aptech.dto.album.AlbumDto;
import com.aptech.dto.artist.SingerDto;
import com.aptech.entity.Album;
import com.aptech.entity.AppStatus;
import com.aptech.entity.Artist;
import com.aptech.entity.Category;
import com.aptech.entity.Genre;
import com.aptech.handle.exception.NotAnImageFileException;
import com.aptech.provider.FileManager;
import com.aptech.repository.AlbumRepository;
import com.aptech.repository.AppStatusRepository;
import com.aptech.repository.ArtistRepository;
import com.aptech.repository.CategoryRepository;
import com.aptech.repository.GenreRepository;
import com.aptech.service.IAlbumService;
import com.aptech.util.AppUtils;

@Service
public class AlbumServiceIpml implements IAlbumService {

	private final Logger logger = LoggerFactory.getLogger(AlbumServiceIpml.class);

	private AlbumRepository albumRepository;
	private CategoryRepository categoryRepository;
	private ArtistRepository artistRepository;
	private GenreRepository genreRepository;
	private AppStatusRepository appStatusRepository;

	private FileManager fileManager;

	@Autowired
	public AlbumServiceIpml(AlbumRepository albumRepository, CategoryRepository categoryRepository,
			ArtistRepository artistRepository, GenreRepository genreRepository, AppStatusRepository appStatusRepository,
			FileManager fileManager) {
		this.albumRepository = albumRepository;
		this.categoryRepository = categoryRepository;
		this.artistRepository = artistRepository;
		this.genreRepository = genreRepository;
		this.appStatusRepository = appStatusRepository;
		this.fileManager = fileManager;
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
			
			if(album == null) {
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

			Long countExistAlbumName = albumRepository.findContainsName(album.getName());

			if (countExistAlbumName > 0) {
				logger.warn("Album name is exist: " + album.getName() + ". Can not handle farther!");
				return new AppServiceResult<AlbumDto>(false, AppError.Validattion.errorCode(),
						"Album name is exist " + album.getName(), null);
			}

			Album newAlbum = new Album();

			newAlbum.setName(album.getName());
			newAlbum.setMusicProduction(album.getMusicProduction());
			newAlbum.setMusicYear(album.getMusicYear());
			newAlbum.getAppStatus();

			Category category = categoryRepository.findById(album.getCategoryId()).orElse(null);
			if (category == null) {
				logger.warn("Category id is not exist: " + album.getCategoryId() + ". Can not handle farther!");
				return new AppServiceResult<AlbumDto>(false, AppError.Validattion.errorCode(),
						"Category id isnot exist: " + album.getCategoryId(), null);
			}

			newAlbum.setCategory(category);
			newAlbum.setActive(false);

			for (Long singerId : album.getSingerIds()) {
				Artist singer = artistRepository.findSingerById(singerId);
				if (singer == null) {
					logger.warn("Singer id is not exist: " + singerId + ". Can not handle farther!");
					return new AppServiceResult<AlbumDto>(false, AppError.Validattion.errorCode(),
							"Singer id is not exist: " + singerId, null);
				} else
					newAlbum.getSingers().add(singer);
			}

			for (Long genreId : album.getGenreIds()) {
				Genre genre = genreRepository.findById(genreId).orElse(null);
				if (genre == null) {
					logger.warn("Genre id is not exist: " + genreId + ". Can not handle farther!");
					return new AppServiceResult<AlbumDto>(false, AppError.Validattion.errorCode(),
							"Genre id is not exist: " + genreId, null);
				} else
					newAlbum.getGenres().add(genre);
			}

			String currentUserLogin = AppUtils.getCurrentUsername();
			newAlbum.setUserNew(currentUserLogin == null ? "system" : currentUserLogin);

			if (album.getImgFile() != null) {

				Path userFolder = Paths
						.get(FileConstant.ALBUM_IMGAGE_FOLDER + AppUtils.normalizeUri(newAlbum.getName()))
						.toAbsolutePath().normalize();

				String imageUrl = fileManager.uploadAlbumImage(userFolder, AppUtils.normalizeUri(newAlbum.getName()),
						album.getImgFile());

				newAlbum.setImgUrl(imageUrl);
			}
			AppStatus defaultStatus = appStatusRepository.getDefaultAppStatus();
			newAlbum.setAppStatus(defaultStatus);

			albumRepository.save(newAlbum);

			final AlbumDto dto = AlbumDto.CreateFromEntity(newAlbum);

			return new AppServiceResult<AlbumDto>(true, 0, "Succeed!", dto);
		} catch (NotAnImageFileException e) {
			e.printStackTrace();

			throw e;
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
			} else
				albumRepository.delete(album);

			return AppBaseResult.GenarateIsSucceed();

		} catch (Exception e) {
			e.printStackTrace();

			return AppBaseResult.GenarateIsFailed(AppError.Unknown.errorCode(), AppError.Unknown.errorMessage());
		}
	}

}
