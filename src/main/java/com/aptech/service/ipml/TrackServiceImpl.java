package com.aptech.service.ipml;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.aptech.constant.AppError;
import com.aptech.domain.AppBaseResult;
import com.aptech.domain.AppServiceResult;
import com.aptech.domain.FullTextSearchWithPagingParam;
import com.aptech.domain.MetaData;
import com.aptech.domain.SearchWithPagingParam;
import com.aptech.dto.pagingation.PageDto;
import com.aptech.dto.pagingation.PageInfo;
import com.aptech.dto.pagingation.PageParam;
import com.aptech.dto.track.TrackCreate;
import com.aptech.dto.track.TrackDto;
import com.aptech.dto.track.TrackShort;
import com.aptech.dto.user.UserWhiteList;
import com.aptech.entity.Album;
import com.aptech.entity.AppStatus;
import com.aptech.entity.AppUser;
import com.aptech.entity.Artist;
import com.aptech.entity.Category;
import com.aptech.entity.Genre;
import com.aptech.entity.Track;
import com.aptech.entity.WhiteList;
import com.aptech.entity.WhiteListId;
import com.aptech.infrastructure.JaudiotaggerParser;
import com.aptech.provider.file.FileServiceFactory;
import com.aptech.provider.file.FileType;
import com.aptech.provider.file.FileService;
import com.aptech.provider.file.MediaFile;
import com.aptech.provider.file.UnsupportedFileTypeException;
import com.aptech.repository.AlbumRepository;
import com.aptech.repository.AppStatusRepository;
import com.aptech.repository.AppUserRepository;
import com.aptech.repository.GenreRepository;
import com.aptech.repository.ArtistRepository;
import com.aptech.repository.CategoryRepository;
import com.aptech.repository.TrackRepository;
import com.aptech.repository.WhiteListRepository;
import com.aptech.service.TrackService;
import com.aptech.util.AppUtils;

@Service
public class TrackServiceImpl implements TrackService {

	private final Logger logger = LoggerFactory.getLogger(TrackServiceImpl.class);

	private TrackRepository trackRepository;
	private AppUserRepository appUserRepository;
	private AlbumRepository albumRepository;
	private GenreRepository genreRepository;
	private AppStatusRepository appStatusRepository;
	private CategoryRepository categoryRepository;
	private ArtistRepository artistRepository;
	private FileService trackFileService;
	private EntityManager entityManager;
	private WhiteListRepository whiteListRepository;

	@Autowired
	public TrackServiceImpl(TrackRepository trackRepository, AppUserRepository appUserRepository,
			AlbumRepository albumRepository, GenreRepository genreRepository, AppStatusRepository appStatusRepository,
			CategoryRepository categoryRepository, ArtistRepository artistRepository, EntityManager entityManager,
			WhiteListRepository whiteListRepository) {
		this.trackRepository = trackRepository;
		this.appUserRepository = appUserRepository;
		this.albumRepository = albumRepository;
		this.genreRepository = genreRepository;
		this.appStatusRepository = appStatusRepository;
		this.categoryRepository = categoryRepository;
		this.artistRepository = artistRepository;
		this.entityManager = entityManager;
		this.whiteListRepository = whiteListRepository;

		this.trackFileService = FileServiceFactory.getFileService(FileType.TRACK);
	}
	
	@Override
	public AppServiceResult<List<TrackDto>> getTracks() {
		try {
			List<Track> entities = trackRepository.findAll();

			List<TrackDto> result = new ArrayList<TrackDto>();

			if (entities != null)
				entities.forEach(item -> result.add(TrackDto.CreateFromEntity(item)));

			return new AppServiceResult<List<TrackDto>>(true, 0, "Succeed!", result);

		} catch (Exception e) {
			e.printStackTrace();
			return new AppServiceResult<List<TrackDto>>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}

	@Override
	public AppServiceResult<TrackDto> addTrack(TrackCreate track) throws UnsupportedFileTypeException {
		try {
			Track newTrack = new Track();
			newTrack.setName(track.getName());

			if (track.getAlbumId() != null) {
				Album album = albumRepository.findById(track.getAlbumId()).orElse(null);

				if (album == null) {
					logger.warn("AlbumId: " + track.getAlbumId() + " is not exist!");

					return new AppServiceResult<TrackDto>(false, AppError.Validattion.errorCode(),
							"AlbumId: " + track.getAlbumId() + " is not exist!", null);
				} else
					newTrack.setAlbum(album);
			}

			newTrack.setMusicProduction(track.getMusicProduction());
			newTrack.setMusicYear(track.getMusicYear());
			newTrack.setLyric(track.getLyric());
			newTrack.setDescription(track.getDescription());
			newTrack.setLiked(0L);
			newTrack.setListened(0L);

			AppUser appUser = appUserRepository.findByUsername(AppUtils.getCurrentUsername());
			newTrack.setAppUser(appUser);
			newTrack.setUserNew(appUser.getUsername());

			Category cat = categoryRepository.findById(track.getCategoryId()).orElse(null);
			if (cat == null) {
				logger.warn("CategoryId: " + track.getCategoryId() + " is not exist!");

				return new AppServiceResult<TrackDto>(false, AppError.Validattion.errorCode(),
						"CategoryId: " + track.getCategoryId() + " is not exist!", null);
			} else
				newTrack.setCategory(cat);

			AppStatus defaultStatus = appStatusRepository.getDefaultAppStatus();
			newTrack.setAppStatus(defaultStatus);

			newTrack.setIsActive(Boolean.FALSE);

			for (Long singerId : track.getSingerIds()) {
				Artist singer = artistRepository.findSingerById(singerId);
				if (singer == null) {
					logger.warn("SingerId: " + singerId + " is not exist!");

					return new AppServiceResult<TrackDto>(false, AppError.Validattion.errorCode(),
							"SingerId: " + singerId + " is not exist!", null);
				} else
					newTrack.getSingers().add(singer);
			}

			for (Long composerId : track.getComposerIds()) {
				Artist composer = artistRepository.findComposerById(composerId);
				if (composer == null) {
					logger.warn("ComposerId: " + composerId + " is not exist!");

					return new AppServiceResult<TrackDto>(false, AppError.Validattion.errorCode(),
							"ComposerId: " + composerId + " is not exist!", null);
				} else
					newTrack.getComposers().add(composer);
			}

			for (Long genreId : track.getGenreIds()) {
				Genre genre = genreRepository.findById(genreId).orElse(null);
				if (genre == null) {
					logger.warn("GenreId: " + genreId + " is not exist!");

					return new AppServiceResult<TrackDto>(false, AppError.Validattion.errorCode(),
							"GenreId: " + genreId + " is not exist!", null);
				} else
					newTrack.getGenres().add(genre);
			}

			if (track.getTrackFile() != null) {
				MediaFile mediaFile = trackFileService.upload(track.getName(), track.getTrackFile());

				newTrack.setTrackPath(mediaFile.getPathFolder());
				newTrack.setTrackUrl(mediaFile.getPathUrl());

				MetaData metaData = JaudiotaggerParser.getRawMetaData(mediaFile.getFile());
				newTrack.setDurationSeconds(metaData.getDurationSeconds());
				newTrack.setBitRate(metaData.getBitRate());

			} else {
				logger.warn("Required audio file!");

				return new AppServiceResult<TrackDto>(false, AppError.Validattion.errorCode(), "Required audio file!",
						null);
			}

			trackRepository.save(newTrack);

			return new AppServiceResult<TrackDto>(true, 0, "Success", TrackDto.CreateFromEntity(newTrack));

		} catch (Exception e) {
			e.printStackTrace();

			return new AppServiceResult<TrackDto>(false, AppError.Unknown.errorCode(), AppError.Unknown.errorMessage(),
					null);
		}
	}

	@Override
	public AppServiceResult<TrackDto> updateTrack() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AppBaseResult removeTrack(Long trackId) {
		try {

			Track track = trackRepository.findById(trackId).orElse(null);
			if (track != null) {
				trackRepository.delete(track);
				trackFileService.remove(track.getTrackPath());
				return AppBaseResult.GenarateIsSucceed();
			} else {
				logger.warn("Track is not exist: " + String.valueOf(trackId) + ", Cannot further process!");
				return AppBaseResult.GenarateIsFailed(AppError.Validattion.errorCode(),
						"Track is not exist: " + String.valueOf(trackId));
			}

		} catch (Exception e) {
			e.printStackTrace();

			return AppBaseResult.GenarateIsFailed(AppError.Unknown.errorCode(), AppError.Unknown.errorMessage());
		}
	}

	@Override
	public AppBaseResult deactiveTrack(Long trackId) {
		try {
			int recordChange = trackRepository.deactiveForId(trackId);

			if (recordChange > 0)
				return AppBaseResult.GenarateIsSucceed();
			else
				return AppBaseResult.GenarateIsFailed(AppError.Validattion.errorCode(),
						AppError.Validattion.errorMessage());
		} catch (Exception e) {
			e.printStackTrace();

			return AppBaseResult.GenarateIsFailed(AppError.Unknown.errorCode(), AppError.Unknown.errorMessage());
		}
	}

	@Override
	public AppServiceResult<List<TrackDto>> getTrackByAppStatus(Long statusId) {
		try {
			List<Track> tracks = trackRepository.findAllByAppStatusId(statusId);

			List<TrackDto> result = new ArrayList<TrackDto>();

			tracks.forEach(item -> {
				result.add(TrackDto.CreateFromEntity(item));
			});

			return new AppServiceResult<List<TrackDto>>(true, 0, "Success", result);

		} catch (Exception e) {
			e.printStackTrace();

			return new AppServiceResult<List<TrackDto>>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}

	@Override
	public AppBaseResult listenedTrack(Long trackId) {
		try {
			int recordChange = trackRepository.addListenedToId(trackId);

			if (recordChange > 0)
				return AppBaseResult.GenarateIsSucceed();
			else
				return AppBaseResult.GenarateIsFailed(AppError.Unknown.errorCode(),
						AppError.Unknown.errorMessage());
		} catch (Exception e) {
			e.printStackTrace();

			return AppBaseResult.GenarateIsFailed(AppError.Unknown.errorCode(), AppError.Unknown.errorMessage());
		}
	}

	@Override
	public AppServiceResult<TrackDto> updateAppStatus(Long trackId, Long statusId) {
		try {
			Track track = trackRepository.findById(trackId).orElse(null);
			if (track == null) {
				logger.warn("Track id is not exist: " + trackId + ". Can not handle farther!");
				return new AppServiceResult<TrackDto>(false, AppError.Validattion.errorCode(),
						"Track id is not exist: " + trackId, null);
			}

			AppStatus appStatus = appStatusRepository.findById(statusId).orElse(null);
			if (appStatus == null) {
				logger.warn("AppStatus id is not exist: " + statusId + ". Can not handle farther!");
				return new AppServiceResult<TrackDto>(false, AppError.Validattion.errorCode(),
						"AppStatus id is not exist: " + statusId, null);
			}

			track.setAppStatus(appStatus);
			track.setIsActive(appStatus.getSetActive());

			trackRepository.save(track);

			return new AppServiceResult<TrackDto>(true, 0, "Succeed!", TrackDto.CreateFromEntity(track));

		} catch (Exception e) {
			e.printStackTrace();

			return new AppServiceResult<TrackDto>(false, AppError.Unknown.errorCode(), AppError.Unknown.errorMessage(),
					null);
		}
	}

	@Override
	public AppServiceResult<TrackDto> getTrack(Long trackId) {
		try {
			Track track = trackRepository.findById(trackId).orElse(null);

			return track == null
					? new AppServiceResult<TrackDto>(false, AppError.Validattion.errorCode(),
							"Track id is not exist: " + trackId, null)
					: new AppServiceResult<TrackDto>(true, 0, "Succeed!", TrackDto.CreateFromEntity(track));

		} catch (Exception e) {
			e.printStackTrace();

			return new AppServiceResult<TrackDto>(false, AppError.Unknown.errorCode(), AppError.Unknown.errorMessage(),
					null);
		}
	}

	@Override
	public AppServiceResult<PageDto<TrackShort>> searchByCategoryAndGenre(SearchWithPagingParam params) {
		try {
			Category category = categoryRepository.findById(params.getCategoryId()).orElse(null);

			Genre genre = params.getGenreId() == null ? null
					: genreRepository.findById(params.getGenreId()).orElse(null);

			if (category == null) {
				logger.warn("CategoryId is not exist: " + params.getCategoryId());

				return new AppServiceResult<PageDto<TrackShort>>(false, AppError.Validattion.errorCode(),
						"CategoryId is not exist: " + params.getCategoryId(), null);
			}

			if (params.getGenreId() != null && genre == null) {
				logger.warn("GenreId is not exist: " + params.getGenreId());

				return new AppServiceResult<PageDto<TrackShort>>(false, AppError.Validattion.errorCode(),
						"GenreId is not exist: " + params.getGenreId(), null);
			}

			Page<Track> results = params.getGenreId() == null
					? trackRepository.findAllByIsActiveTrueAndCategory(category, params.getPageParam().getPageable())
					: trackRepository.findAllByIsActiveTrueAndCategoryAndGenres(category, genre,
							params.getPageParam().getPageable());

			Page<TrackShort> dtoPage = results.map(item -> TrackShort.CreateFromEntity(item));

			return new AppServiceResult<PageDto<TrackShort>>(true, 0, "Succeed!", new PageDto<TrackShort>(dtoPage));

		} catch (Exception e) {
			e.printStackTrace();

			return new AppServiceResult<PageDto<TrackShort>>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public AppServiceResult<PageDto<TrackShort>> searchByFTS(FullTextSearchWithPagingParam params) {
		try {
			FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(entityManager);

			QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder()
					.forEntity(Track.class).get();

			org.apache.lucene.search.Query combinedQuery = queryBuilder.keyword()
					.onFields("name", "album.name", "singers.nickName").ignoreAnalyzer().matching(params.getText()).createQuery();

			org.hibernate.search.jpa.FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(combinedQuery,
					Track.class);
			jpaQuery.setFirstResult(params.getPageParam().getPageIndex() * params.getPageParam().getPageSize());
			jpaQuery.setMaxResults(params.getPageParam().getPageSize());
			
			List<Track> tracks = jpaQuery.getResultList();

			PageDto<TrackShort> result = new PageDto<TrackShort>();
			result.setContent(
					tracks.stream().map(item -> TrackShort.CreateFromEntity(item)).collect(Collectors.toList()));
			PageInfo pageInfo = new PageInfo();
			pageInfo.setCurrentPage(params.getPageParam().getPageIndex());
			pageInfo.setPageSize(params.getPageParam().getPageSize());
			pageInfo.setTotalElements((long) jpaQuery.getResultSize());

			result.setPageInfo(pageInfo);

			return new AppServiceResult<PageDto<TrackShort>>(true, 0, "Secceed!", result);

		} catch (Exception e) {
			e.printStackTrace();

			return new AppServiceResult<PageDto<TrackShort>>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}

	@Override
	public AppServiceResult<List<TrackShort>> getTopHitByCategory(Long categoryId) {
		try {
			Category category = categoryRepository.findById(categoryId).orElse(null);

			if (category == null) {
				logger.warn("CategoryId is not exist: " + categoryId);

				return new AppServiceResult<List<TrackShort>>(false, AppError.Validattion.errorCode(),
						"CategoryId is not exist: " + categoryId, null);
			}

			List<Track> tracks = trackRepository.findTop20ByIsActiveTrueAndCategoryOrderByListenedDesc(category);
			List<TrackShort> result = new ArrayList<TrackShort>();

			if (tracks != null)
				tracks.forEach(item -> {
					result.add(TrackShort.CreateFromEntity(item));
				});

			return new AppServiceResult<List<TrackShort>>(true, 0, "Succeed!", result);

		} catch (Exception e) {
			e.printStackTrace();

			return new AppServiceResult<List<TrackShort>>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}

	@Override
	public AppServiceResult<PageDto<TrackShort>> getAllTrackLiked(PageParam pageParam) {
		try {
			AppUser appUser = appUserRepository.findByUsername(AppUtils.getCurrentUsername());

			if (appUser == null) {
				logger.warn("Not logged in!");

				return new AppServiceResult<PageDto<TrackShort>>(false, AppError.Validattion.errorCode(),
						"Not logged in!", null);
			}

			Page<Track> results = trackRepository.findWhiteListByUser(appUser, pageParam.getPageable());
			Page<TrackShort> dtoPage = results.map(item -> TrackShort.CreateFromEntity(item));

			return new AppServiceResult<PageDto<TrackShort>>(true, 0, "Succeed!", new PageDto<TrackShort>(dtoPage));

		} catch (Exception e) {
			e.printStackTrace();

			return new AppServiceResult<PageDto<TrackShort>>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}

	@Override
	public AppServiceResult<Long[]> getTrackIdsLiked() {

		try {
			AppUser appUser = appUserRepository.findByUsername(AppUtils.getCurrentUsername());

			if (appUser == null) {
				logger.warn("Not logged in!");

				return new AppServiceResult<Long[]>(false, AppError.Validattion.errorCode(), "Not logged in!", null);
			}

			List<Long> listTrackId = trackRepository.findAllTrackIdInWhiteListByUser(appUser);
			Long[] result = new Long[listTrackId.size()];
			result = listTrackId.toArray(result);

			return new AppServiceResult<Long[]>(true, 0, "Succeed", result);

		} catch (Exception e) {
			e.printStackTrace();

			return new AppServiceResult<Long[]>(false, AppError.Unknown.errorCode(), AppError.Unknown.errorMessage(),
					null);
		}
	}

	@Override
	@Transactional
	public AppBaseResult updateWhiteList(UserWhiteList dto) {

		try {
			AppUser appUser = appUserRepository.findByUsername(AppUtils.getCurrentUsername());
			if (appUser == null) {
				logger.warn("Not logged in!");

				return AppBaseResult.GenarateIsFailed(AppError.Validattion.errorCode(), "Not logged in!");
			}

			Track track = trackRepository.findById(dto.getTrackId()).orElse(null);

			if (track == null) {
				logger.warn("TrackId is not exist: " + dto.getTrackId() + ", Cannot further process!");

				return AppBaseResult.GenarateIsFailed(AppError.Validattion.errorCode(),
						"TrackId is not exist: " + dto.getTrackId());
			}

			boolean existWhiteList = whiteListRepository.existsByAppUserAndTrack(appUser, track);
			if (dto.getIsAdd()) {
				if (existWhiteList) {
					return AppBaseResult.GenarateIsFailed(AppError.Validattion.errorCode(),
							"WhiteList is exist!");
				} else {

					WhiteList newWL = new WhiteList();
					newWL.setAppUser(appUser);
					newWL.setTrack(track);
					newWL.setWhiteListId(new WhiteListId(appUser.getId(), track.getId()));

					whiteListRepository.save(newWL);
					trackRepository.addLikedToId(track.getId());
				}
			} else {
				if (existWhiteList) {
					boolean removed = whiteListRepository.deleteByAppUserAndTrack(appUser, track) > 0;
					if (removed)
						trackRepository.removeLikedToId(track.getId());
				} else {
					logger.warn("WhiteList is not exist!, Cannot further process!");

					return AppBaseResult.GenarateIsFailed(AppError.Validattion.errorCode(),
							"WhiteList is not exist!");
				}
			}

			return AppBaseResult.GenarateIsSucceed();
		} catch (Exception e) {
			e.printStackTrace();

			return AppBaseResult.GenarateIsFailed(AppError.Unknown.errorCode(), AppError.Unknown.errorMessage());
		}
	}
}
