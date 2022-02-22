package com.aptech.service.ipml;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aptech.constant.AppError;
import com.aptech.constant.FileConstant;
import com.aptech.domain.AppServiceResult;
import com.aptech.dto.AlbumDto;
import com.aptech.dto.AlbumRes;
import com.aptech.entity.Album;
import com.aptech.entity.AppUser;
import com.aptech.handle.exception.NotAnImageFileException;
import com.aptech.provider.FileManager;
import com.aptech.repository.AlbumRepository;
import com.aptech.repository.AppUserRepository;
import com.aptech.service.IAlbumService;
import com.aptech.util.AppUtils;

@Service
public class AlbumServiceIpml implements IAlbumService {

	private final Logger logger = LoggerFactory.getLogger(AlbumServiceIpml.class);

	private AlbumRepository albumRepository;
	private AppUserRepository appUserRepository;
	private FileManager fileManager;

	@Autowired
	public AlbumServiceIpml(AlbumRepository albumRepository, AppUserRepository appUserRepository,
			FileManager fileManager) {
		this.albumRepository = albumRepository;
		this.appUserRepository = appUserRepository;
		this.fileManager = fileManager;
	}

	@Override
	public AppServiceResult<AlbumRes> getAlbum(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AppServiceResult<List<AlbumRes>> getAlbumForUserId(Long userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AppServiceResult<AlbumRes> addAlbum(AlbumDto albumDto) throws NotAnImageFileException {
		try {
			// TODO: Mapping
			Album newAlbum = new Album();
			newAlbum.setName(albumDto.getName());
			newAlbum.setDescription(albumDto.getDescription());
			newAlbum.setReleaseDate(albumDto.getReleaseDate());

			if (AppUtils.getCurrentUsername() != null) {
				AppUser user = appUserRepository.findByUsername(AppUtils.getCurrentUsername());

				newAlbum.setAppUser(user);
			}

			if (albumDto.getImageFile() != null) {
				Path userFolder = Paths
						.get(FileConstant.ALBUM_IMGAGE_FOLDER + AppUtils.normalizeUri(newAlbum.getName()))
						.toAbsolutePath().normalize();

				String imageUrl = fileManager.uploadAlbumImage(userFolder, AppUtils.normalizeUri(newAlbum.getName()),
						albumDto.getImageFile());
				newAlbum.setImageUrl(imageUrl);
			}

			albumRepository.save(newAlbum);

			AlbumRes dto = new AlbumRes(newAlbum.getId(), newAlbum.getName(), newAlbum.getDescription(), newAlbum.getReleaseDate(), newAlbum.getImageUrl());
			return new AppServiceResult<AlbumRes>(true, 0, "success", dto);
		} catch (NotAnImageFileException e) {
			e.printStackTrace();

			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());

			return new AppServiceResult<AlbumRes>(true, AppError.Unknown.errorCode(), AppError.Unknown.errorMessage(),
					null);
		}

	}

	@Override
	public AppServiceResult<AlbumRes> deleteAlbum(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
