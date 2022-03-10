package com.aptech.service.ipml;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.aptech.constant.AppError;
import com.aptech.constant.RoleConstant;
import com.aptech.domain.AppBaseResult;
import com.aptech.domain.AppServiceResult;
import com.aptech.dto.pagingation.PageDto;
import com.aptech.dto.pagingation.PageParam;
import com.aptech.dto.playlist.PlaylistCreate;
import com.aptech.dto.playlist.PlaylistDetailUpdate;
import com.aptech.dto.playlist.PlaylistDto;
import com.aptech.dto.playlist.PlaylistShort;
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
import com.aptech.repository.PlaylistDetailRepository;
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
	private PlaylistDetailRepository playlistDetailRepository;
	private AppUserRepository appUserRepository;
	private CategoryRepository categoryRepository;
	private GenreRepository genreRepository;
	private FileService imageFileService;
	private TrackRepository trackRepository;

	@Autowired
	public PlaylistServiceImpl(PlaylistTypeRepository playlistTypeRepository, PlaylistRepository playlistRepository,
			PlaylistDetailRepository playlistDetailRepository, AppUserRepository appUserRepository,
			CategoryRepository categoryRepository, GenreRepository genreRepository, TrackRepository trackRepository) {
		this.appUserRepository = appUserRepository;
		this.playlistRepository = playlistRepository;
		this.playlistDetailRepository = playlistDetailRepository;
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

			if (playlistNew.getPlaylistTypeId() != null) {
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

			Track track = trackRepository.findById(dto.getTrackId()).orElse(null);
			if (track == null) {
				logger.warn("TrackId is not exist!, " + dto.getTrackId());

				return AppBaseResult.GenarateIsFailed(AppError.Validattion.errorCode(),
						"TrackId is not exist!, " + dto.getTrackId());
			}

			if (dto.getIsRemove() == Boolean.TRUE) {
				playlistDetailRepository.deleteByPlaylistAndTrack(playlist, track);
			} else {
				boolean existByPlaylistAndTrack = playlistDetailRepository.existByPlaylistAndTrack(playlist, track);

				if (existByPlaylistAndTrack) {
					logger.warn("TrackId is exist in playlist!, " + dto.getTrackId());

					return AppBaseResult.GenarateIsFailed(AppError.Validattion.errorCode(),
							"TrackId is exist in playlist!, " + dto.getTrackId());
				} else {
					PlaylistDetail detail = new PlaylistDetail();
					detail.setPlaylist(playlist);
					detail.setTrack(track);

					playlistDetailRepository.save(detail);
				}
			}

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

	@Override
	public AppServiceResult<List<PlaylistShort>> getPlaylistByType(Long playlistTypeId) {
		try {
			PlaylistType type = playlistTypeRepository.findById(playlistTypeId).orElse(null);
			if (type == null) {
				logger.warn("PlaylistTypeId is not exist!, " + playlistTypeId);

				return new AppServiceResult<List<PlaylistShort>>(false, AppError.Validattion.errorCode(),
						"PlaylistTypeId is not exist!, " + playlistTypeId, null);
			}

			List<Playlist> playlists = playlistRepository.findAllByPlaylistTypeAndIsPublicTrue(type);
			List<PlaylistShort> result = new ArrayList<PlaylistShort>();
			
			if(playlists != null)
				playlists.forEach(item -> result.add(PlaylistShort.CreateFromEntity(item)));
			
			return new AppServiceResult<List<PlaylistShort>>(true, 0, "Succeed!", result);

		} catch (Exception e) {
			e.printStackTrace();

			return new AppServiceResult<List<PlaylistShort>>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}

	@Override
	public AppServiceResult<PageDto<PlaylistShort>> getPlaylistByUserLoggedIn(PageParam pageParam) {
		try {
			AppUser userLoggedIn = appUserRepository.findByUsername(AppUtils.getCurrentUsername());
			if (userLoggedIn == null) {
				logger.warn("Current user is null!");

				return new AppServiceResult<PageDto<PlaylistShort>>(false, AppError.Validattion.errorCode(),
						"Do not get current user!", null);
			}

			Page<Playlist> playlists = playlistRepository.findAllByAppUser(userLoggedIn, pageParam.getPageable()); //OrderByDateNewDesc

			Page<PlaylistShort> dtoPage = playlists.map(item -> PlaylistShort.CreateFromEntity(item));
			
			return new AppServiceResult<PageDto<PlaylistShort>>(true, 0, "Succeed!", new PageDto<PlaylistShort>(dtoPage));

		} catch (Exception e) {
			e.printStackTrace();

			return new AppServiceResult<PageDto<PlaylistShort>>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}
}
