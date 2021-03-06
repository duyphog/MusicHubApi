package com.aptech.service.ipml;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aptech.constant.AppError;
import com.aptech.domain.AppServiceResult;
import com.aptech.dto.appsatus.AppStatusDto;
import com.aptech.dto.artist.ComposerDto;
import com.aptech.dto.artist.SingerDto;
import com.aptech.dto.category.CategoryDto;
import com.aptech.dto.genre.GenreDto;
import com.aptech.dto.playlist.PlaylistTypeDto;
import com.aptech.entity.AppStatus;
import com.aptech.entity.Artist;
import com.aptech.entity.Category;
import com.aptech.entity.Genre;
import com.aptech.entity.PlaylistType;
import com.aptech.repository.AppStatusRepository;
import com.aptech.repository.ArtistRepository;
import com.aptech.repository.CategoryRepository;
import com.aptech.repository.GenreRepository;
import com.aptech.repository.PlaylistTypeRepository;
import com.aptech.service.CommonService;

@Service
public class CommonServiceImpl implements CommonService {

	private ArtistRepository artistRepository;
	private CategoryRepository categoryRepository;
	private GenreRepository genreRepository;
	private AppStatusRepository appStatusRepository;
	private PlaylistTypeRepository playlistTypeRepository;

	@Autowired
	public CommonServiceImpl(ArtistRepository artistRepository, CategoryRepository categoryRepository,
			GenreRepository genreRepository, AppStatusRepository appStatusRepository,
			PlaylistTypeRepository playlistTypeRepository) {
		this.artistRepository = artistRepository;
		this.categoryRepository = categoryRepository;
		this.genreRepository = genreRepository;
		this.appStatusRepository = appStatusRepository;
		this.playlistTypeRepository = playlistTypeRepository;

	}

	@Override
	public AppServiceResult<List<SingerDto>> searchSinger(String searchString) {
		try {
			List<Artist> singers = artistRepository.findSingerByNickNameContaining(searchString);

			List<SingerDto> result = new ArrayList<SingerDto>();

			singers.forEach(item -> {
				result.add(SingerDto.CreateFromEntity(item));
			});

			return new AppServiceResult<List<SingerDto>>(true, 0, "Succeed!", result);

		} catch (Exception e) {
			e.printStackTrace();
			return new AppServiceResult<List<SingerDto>>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}

	@Override
	public AppServiceResult<List<ComposerDto>> searchComposer(String searchString) {
		try {
			List<Artist> composers = artistRepository.findComposerByNickNameContaining(searchString);

			List<ComposerDto> result = new ArrayList<ComposerDto>();

			composers.forEach(item -> {
				result.add(ComposerDto.CreateFromEntity(item));
			});

			return new AppServiceResult<List<ComposerDto>>(true, 0, "Succeed!", result);

		} catch (Exception e) {
			e.printStackTrace();
			return new AppServiceResult<List<ComposerDto>>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}

	@Override
	public AppServiceResult<List<CategoryDto>> getCategories() {
		try {
			List<Category> categories = categoryRepository.findAll();

			List<CategoryDto> result = new ArrayList<CategoryDto>();

			categories.forEach(item -> {
				result.add(CategoryDto.CreateFromEntity(item));
			});

			return new AppServiceResult<List<CategoryDto>>(true, 0, "Succeed!", result);

		} catch (Exception e) {
			e.printStackTrace();
			return new AppServiceResult<List<CategoryDto>>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}

	@Override
	public AppServiceResult<List<GenreDto>> getGenres() {
		try {
			List<Genre> genres = genreRepository.findAll();

			List<GenreDto> result = new ArrayList<GenreDto>();

			genres.forEach(item -> {
				result.add(GenreDto.CreateFromEntity(item));
			});

			return new AppServiceResult<List<GenreDto>>(true, 0, "Succeed!", result);

		} catch (Exception e) {
			e.printStackTrace();
			return new AppServiceResult<List<GenreDto>>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}

	@Override
	public AppServiceResult<List<AppStatusDto>> getAppStatus() {
		try {
			List<AppStatus> entities = appStatusRepository.findAll();

			List<AppStatusDto> result = new ArrayList<AppStatusDto>();

			entities.forEach(item -> {
				result.add(AppStatusDto.CreateFromEntity(item));
			});

			return new AppServiceResult<List<AppStatusDto>>(true, 0, "Succeed!", result);

		} catch (Exception e) {
			e.printStackTrace();
			return new AppServiceResult<List<AppStatusDto>>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}

	@Override
	public AppServiceResult<List<PlaylistTypeDto>> getPlaylistTypes() {
		try {
			List<PlaylistType> entities = playlistTypeRepository.findAll();

			List<PlaylistTypeDto> result = new ArrayList<PlaylistTypeDto>();

			entities.forEach(item -> {
				result.add(PlaylistTypeDto.CreateFromEntity(item));
			});

			return new AppServiceResult<List<PlaylistTypeDto>>(true, 0, "Succeed!", result);

		} catch (Exception e) {
			e.printStackTrace();
			return new AppServiceResult<List<PlaylistTypeDto>>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}

}
