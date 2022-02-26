package com.aptech.service.ipml;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aptech.constant.AppError;
import com.aptech.constant.FileConstant;
import com.aptech.domain.AppBaseResult;
import com.aptech.domain.AppServiceResult;
import com.aptech.dto.track.TrackCreate;
import com.aptech.dto.track.TrackDto;
import com.aptech.entity.Album;
import com.aptech.entity.AppStatus;
import com.aptech.entity.AppUser;
import com.aptech.entity.Artist;
import com.aptech.entity.Category;
import com.aptech.entity.Genre;
import com.aptech.entity.Track;
import com.aptech.handle.exception.NotAnAudioFileException;
import com.aptech.provider.FileManager;
import com.aptech.repository.AlbumRepository;
import com.aptech.repository.AppStatusRepository;
import com.aptech.repository.AppUserRepository;
import com.aptech.repository.GenreRepository;
import com.aptech.repository.ArtistRepository;
import com.aptech.repository.CategoryRepository;
import com.aptech.repository.TrackRepository;
import com.aptech.service.ITrackService;
import com.aptech.util.AppUtils;

@Service
public class TrackServiceImpl implements ITrackService {

	private final Logger logger = LoggerFactory.getLogger(TrackServiceImpl.class);

	private TrackRepository trackRepository;
	private AppUserRepository appUserRepository;
	private AlbumRepository albumRepository;
	private GenreRepository genreRepository;
	private AppStatusRepository appStatusRepository;
	private CategoryRepository categoryRepository;
	private ArtistRepository artistRepository;

	private FileManager fileManager;

	@Autowired
	public TrackServiceImpl(TrackRepository trackRepository, AppUserRepository appUserRepository,
			FileManager fileManager, AlbumRepository albumRepository, GenreRepository genreRepository,
			AppStatusRepository appStatusRepository, CategoryRepository categoryRepository,
			ArtistRepository artistRepository) {
		this.trackRepository = trackRepository;
		this.appUserRepository = appUserRepository;
		this.fileManager = fileManager;
		this.albumRepository = albumRepository;
		this.genreRepository = genreRepository;
		this.appStatusRepository = appStatusRepository;
		this.categoryRepository = categoryRepository;
		this.artistRepository = artistRepository;
	}

	@Override
	public AppServiceResult<TrackDto> getTrack() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AppServiceResult<TrackDto> addTrack(TrackCreate track) throws NotAnAudioFileException {
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

			AppUser appUser = appUserRepository.findByUsername(AppUtils.getCurrentUsername());
			newTrack.setAppUser(appUser);

			Category cat = categoryRepository.findById(track.getCategoryId()).orElse(null);
			if (cat == null) {
				logger.warn("CategoryId: " + track.getCategoryId() + " is not exist!");

				return new AppServiceResult<TrackDto>(false, AppError.Validattion.errorCode(),
						"CategoryId: " + track.getCategoryId() + " is not exist!", null);
			} else
				newTrack.setCategory(cat);

			AppStatus defaultStatus = appStatusRepository.getDefaultAppStatus();
			newTrack.setAppStatus(defaultStatus);

			newTrack.setActive(false);

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
					newTrack.getGenre().add(genre);
			}

			if (track.getTrackFile() != null) {
				Path songFolder = Paths.get(FileConstant.TRACK_FOLDER).toAbsolutePath().normalize();

				String trackUrl = fileManager.uploadAudioFile(songFolder, AppUtils.getCurrentUsername(),
						AppUtils.normalizeUri(track.getName()), track.getTrackFile());

				newTrack.setTrackUrl(trackUrl);
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

			Track track = trackRepository.getById(trackId);
			if (track != null) {
				trackRepository.delete(track);

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
			int recordChange = trackRepository.DeactiveForId(trackId);

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
	public AppBaseResult likedTrack(Long trackId, boolean state) {
		try {
			int recordChange = 0;
			if (state)
				recordChange = trackRepository.AddLikedToId(trackId);
			else
				recordChange = trackRepository.RemoveLikedToId(trackId);

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
	public AppBaseResult listenedTrack(Long trackId) {
		try {
			int recordChange = trackRepository.AddListenedToId(trackId);

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

}
