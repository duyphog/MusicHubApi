package com.aptech.service.ipml;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aptech.constant.AppError;
import com.aptech.constant.RoleConstant;
import com.aptech.domain.AppBaseResult;
import com.aptech.domain.AppServiceResult;
import com.aptech.dto.playlist.PlaylistCreate;
import com.aptech.dto.playlist.PlaylistDetailUpdate;
import com.aptech.dto.playlist.PlaylistDto;
import com.aptech.entity.AppRole;
import com.aptech.entity.AppUser;
import com.aptech.entity.Category;
import com.aptech.entity.Genre;
import com.aptech.entity.Playlist;
import com.aptech.entity.PlaylistDetail;
import com.aptech.entity.PlaylistType;
import com.aptech.entity.Track;
import com.aptech.provider.file.FileService;
import com.aptech.provider.file.FileServiceFactory;
import com.aptech.provider.file.FileType;
import com.aptech.provider.file.MediaFile;
import com.aptech.repository.AppUserRepository;
import com.aptech.repository.CategoryRepository;
import com.aptech.repository.GenreRepository;
import com.aptech.repository.PlaylistRepository;
import com.aptech.repository.PlaylistTypeRepository;
import com.aptech.repository.TrackRepository;
import com.aptech.service.PlaylistService;
import com.aptech.util.AppUtils;

@Service
public class PlaylistServiceImpl implements PlaylistService {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private PlaylistTypeRepository playlistTypeRepository;
	private PlaylistRepository playlistRepository;
	private AppUserRepository appUserRepository;
	private CategoryRepository categoryRepository;
	private GenreRepository genreRepository;
	private FileService imageFileService;
	private TrackRepository trackRepository;

	@Autowired
	public PlaylistServiceImpl(PlaylistTypeRepository playlistTypeRepository, PlaylistRepository playlistRepository,
			AppUserRepository appUserRepository, CategoryRepository categoryRepository, GenreRepository genreRepository,
			TrackRepository trackRepository) {
		this.appUserRepository = appUserRepository;
		this.playlistRepository = playlistRepository;
		this.playlistTypeRepository = playlistTypeRepository;
		this.categoryRepository = categoryRepository;
		this.genreRepository = genreRepository;
		this.trackRepository = trackRepository;

		this.imageFileService = FileServiceFactory.getFileService(FileType.IMAGE);
	}

	@Override
	public AppServiceResult<PlaylistDto> getPlaylist(Long playlistId) {
		try {
			Playlist playlist = playlistRepository.findById(playlistId).orElse(null);
			if (playlist == null) {
				logger.warn("PlaylistId is not exist!, " + playlistId);

				return new AppServiceResult<PlaylistDto>(false, AppError.Validattion.errorCode(),
						"PlaylistId is not exist!, " + playlistId, null);
			}

			return new AppServiceResult<PlaylistDto>(true, 0, "Succeed!", PlaylistDto.CreateFromEntity(playlist));

		} catch (Exception e) {
			e.printStackTrace();

			return new AppServiceResult<PlaylistDto>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}

	@Override
	public AppServiceResult<PlaylistDto> createPlaylist(PlaylistCreate playlistNew) {
		try {

			Playlist playlist = playlistRepository.findByName(playlistNew.getName());
			if (playlist != null) {
				logger.warn("Playlist name is exist!, " + playlistNew.getName());

				return new AppServiceResult<PlaylistDto>(false, AppError.Validattion.errorCode(),
						"Playlist name is exist!, " + playlistNew.getName(), null);
			}

			Playlist newPlaylist = new Playlist();
			newPlaylist.setName(playlistNew.getName());
			
			newPlaylist.setDescription(playlistNew.getDescription());
			newPlaylist.setLiked(0L);
			newPlaylist.setListened(0L);

			if(playlistNew.getPlaylistTypeId() != null) {
				PlaylistType type = playlistTypeRepository.findById(playlistNew.getPlaylistTypeId()).orElse(null);
				if (type == null) {
					logger.warn("PlaylistTypeId is not exist!, " + playlistNew.getPlaylistTypeId());

					return new AppServiceResult<PlaylistDto>(false, AppError.Validattion.errorCode(),
							"PlaylistTypeId is not exist!, " + playlistNew.getPlaylistTypeId(), null);
				}
				
				newPlaylist.setPlaylistType(type);
			}
			
			AppUser currentUser = appUserRepository.findByUsername(AppUtils.getCurrentUsername());
			if (currentUser == null) {
				logger.warn("Current user is null!");

				return new AppServiceResult<PlaylistDto>(false, AppError.Validattion.errorCode(),
						"Do not get current user!", null);
			}

			newPlaylist.setIsPublic(Boolean.FALSE);
			newPlaylist.setAppUser(currentUser);
			newPlaylist.setUserNew(currentUser.getUsername());

			if (currentUser.getAppRoles() != null) {
				Set<AppRole> roles = currentUser.getAppRoles();
				roles.forEach(item -> {
					if (item.getName().equals(RoleConstant.ROLE_ADMIN))
						newPlaylist.setIsPublic(Boolean.TRUE);
				});
			}

			if (playlistNew.getCategoryId() != null) {
				Category cat = categoryRepository.findById(playlistNew.getCategoryId()).orElse(null);
				if (cat == null) {
					logger.warn("CategoryId is not exist!, " + playlistNew.getCategoryId());

					return new AppServiceResult<PlaylistDto>(false, AppError.Validattion.errorCode(),
							"CategoryId is not exist!, " + playlistNew.getCategoryId(), null);
				}

				newPlaylist.setCategory(cat);
			}

			if (playlistNew.getGenreId() != null) {
				Genre genre = genreRepository.findById(playlistNew.getGenreId()).orElse(null);
				if (genre == null) {
					logger.warn("GenreId is not exist!, " + playlistNew.getGenreId());

					return new AppServiceResult<PlaylistDto>(false, AppError.Validattion.errorCode(),
							"GenreId is not exist!, " + playlistNew.getGenreId(), null);
				}

				newPlaylist.setGenre(genre);
			}

			if (playlistNew.getImgFile() != null) {
				MediaFile mediaFile = imageFileService.upload(playlistNew.getName(), playlistNew.getImgFile());
				newPlaylist.setImageUrl(mediaFile.getPathUrl());
				newPlaylist.setImagePath(mediaFile.getPathFolder());
			}

			playlistRepository.save(newPlaylist);

			return new AppServiceResult<PlaylistDto>(true, 0, "Succeed!", PlaylistDto.CreateFromEntity(newPlaylist));

		} catch (Exception e) {
			e.printStackTrace();

			logger.error(e.getMessage());

			return new AppServiceResult<PlaylistDto>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}

	@Override
	public AppBaseResult updateTrackToPlaylistDetail(PlaylistDetailUpdate dto) {
		try {
			Playlist playlist = playlistRepository.findById(dto.getPlaylistId()).orElse(null);
			if (playlist == null) {
				logger.warn("PlaylistId is not exist!, " + dto.getPlaylistId());

				return AppBaseResult.GenarateIsFailed(AppError.Validattion.errorCode(),
						"PlaylistId is not exist!, " + dto.getPlaylistId());
			}

			if (dto.getIsRemove() == Boolean.TRUE) {
				playlist.getPlaylistDetails().removeIf(item -> item.getTrack().getId() == dto.getTrackId());
			} else {
				Track track = trackRepository.findById(dto.getTrackId()).orElse(null);
				if (track == null) {
					logger.warn("TrackId is not exist!, " + dto.getTrackId());

					return AppBaseResult.GenarateIsFailed(AppError.Validattion.errorCode(),
							"TrackId is not exist!, " + dto.getTrackId());
				}

				PlaylistDetail detail = new PlaylistDetail();
				detail.setPlaylist(playlist);
				detail.setTrack(track);

				playlist.getPlaylistDetails().add(detail);
			}

			playlistRepository.save(playlist);

			return AppBaseResult.GenarateIsSucceed();

		} catch (Exception e) {
			e.printStackTrace();

			return AppBaseResult.GenarateIsFailed(AppError.Unknown.errorCode(), AppError.Unknown.errorMessage());
		}
	}

	@Override
	public AppBaseResult removePlaylist(Long playlistId) {
		try {
			playlistRepository.deleteById(playlistId);

			return AppBaseResult.GenarateIsSucceed();

		} catch (Exception e) {
			e.printStackTrace();

			return AppBaseResult.GenarateIsFailed(AppError.Unknown.errorCode(), AppError.Unknown.errorMessage());
		}
	}

}
