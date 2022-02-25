package com.aptech.service.ipml;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aptech.constant.AppError;
import com.aptech.constant.FileConstant;
import com.aptech.domain.AppBaseResult;
import com.aptech.domain.AppServiceResult;
import com.aptech.dto.genre.GenreDto;
import com.aptech.dto.track.CreateTrack;
import com.aptech.dto.track.TrackDto;
import com.aptech.entity.Album;
import com.aptech.entity.AppUser;
import com.aptech.entity.Singer;
import com.aptech.entity.Track;
import com.aptech.handle.exception.NotAnAudioFileException;
import com.aptech.handle.exception.NotAnImageFileException;
import com.aptech.provider.FileManager;
import com.aptech.repository.AlbumRepository;
import com.aptech.repository.AppUserRepository;
import com.aptech.repository.GenreRepository;
import com.aptech.repository.ArtistRepository;
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
	private ArtistRepository singerRepository;
	private ModelMapper modelMapper;

	private FileManager fileManager;

	@Autowired
	public TrackServiceImpl(TrackRepository trackRepository, AppUserRepository appUserRepository,
			FileManager fileManager, AlbumRepository albumRepository, GenreRepository genreRepository,
			ArtistRepository singerRepository, ModelMapper modelMapper
			) {
		this.trackRepository = trackRepository;
		this.appUserRepository = appUserRepository;
		this.fileManager = fileManager;
		this.albumRepository = albumRepository;
		this.genreRepository = genreRepository;
		this.singerRepository = singerRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public AppServiceResult<TrackDto> getTrack() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AppServiceResult<TrackDto> addTrack(CreateTrack track)
			throws NotAnImageFileException, NotAnAudioFileException {
		try {
			AppUser appUser = appUserRepository.findByUsername(AppUtils.getCurrentUsername());

			Track newTrack = new Track();
			newTrack.setName(track.getName());
			newTrack.setDescription(track.getDescription());
			newTrack.setAppUser(appUser);
			newTrack.setReleaseDate(AppUtils.ParseDateString(track.getReleaseDate()));

			newTrack.setGenre(genreRepository.findById(track.getGenreId()).orElse(null));

			for (Long singerId : track.getSingerIds()) {
//				Singer singer = singerRepository.findById(singerId).orElse(null);
//				if(singer == null) {
//					logger.warn("SingerId: "+ singerId + " is not exist!");
//
//					return new AppServiceResult<TrackDto>(false, AppError.Unknown.errorCode(), "SingerId: "+ singerId + " is not exist!",
//							null);
//				}
//				else
//					newTrack.AddSinger(singer);
			}

			if (track.getComposerId() != null) {
				AppUser composer = appUserRepository.findById(track.getComposerId()).orElse(null);
				newTrack.setComposer(composer);
			}

			if (track.getAlbumId() != null) {
				Album album = albumRepository.findById(track.getAlbumId()).orElse(null);
				newTrack.setAlbum(album);
			}

			if (track.getImageFile() != null) {
				Path imageFolder = Paths.get(FileConstant.TRACK_IMGAGE_FOLDER + AppUtils.normalizeUri(track.getName()))
						.toAbsolutePath().normalize();

				String imageUrl = fileManager.uploadSongImage(imageFolder, AppUtils.normalizeUri(track.getName()),
						track.getImageFile());

				newTrack.setImageUrl(imageUrl);
			}

			if (track.getTrackFile() != null) {
				Path songFolder = Paths.get(FileConstant.TRACK_FOLDER).toAbsolutePath().normalize();

				String trackUrl = fileManager.uploadAudioFile(songFolder, AppUtils.getCurrentUsername(),
						AppUtils.normalizeUri(track.getName()), track.getTrackFile());

				newTrack.setTrackUrl(trackUrl);
			}

			trackRepository.save(newTrack);

//			TrackDto trackDto = new TrackDto(newTrack.getId(), newTrack.getName(), newTrack.getReleaseDate(),
//					newTrack.getComposer().getUsername(), newTrack.getComposer().getId(),
//					new GenreDto(newTrack.getGenre().getId(), newTrack.getGenre().getName(), ""),
//					newTrack.getDescription(), newTrack.getImageUrl(), newTrack.getTrackUrl(),
//					new AlbumRes(newTrack.getAlbum().getId(), newTrack.getAlbum().getName(),
//							newTrack.getAlbum().getDescription(), newTrack.getAlbum().getReleaseDate(),
//							newTrack.getAlbum().getImageUrl()),
//					newTrack.getLiked(), newTrack.getListened());
		
				
			
			return new AppServiceResult<TrackDto>(true, 0, "Success", TrackDto.CreateFromEntity(newTrack));

		} catch (Exception e) {
			e.printStackTrace();

			logger.error(e.getMessage());

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
	public AppBaseResult removeTrack(long trackId) {
		try {

			Track track = trackRepository.getById(trackId);
			if (track != null) {
				trackRepository.delete(track);

				return new AppBaseResult(true, 0, null);
			} else {
				logger.warn("Track is not exist: " + String.valueOf(trackId) + ", Cannot further process!");
				return new AppBaseResult(false, AppError.Validattion.errorCode(),
						"Track is not exist: " + String.valueOf(trackId));
			}

		} catch (Exception e) {
			e.printStackTrace();

			logger.error(e.getMessage());

			return new AppBaseResult(false, AppError.Unknown.errorCode(), AppError.Unknown.errorMessage());
		}
	}

	@Override
	public AppBaseResult likedTrack(boolean state) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AppBaseResult listenedTrack(Long trackId) {
		// TODO Auto-generated method stub
		return null;
	}

}
