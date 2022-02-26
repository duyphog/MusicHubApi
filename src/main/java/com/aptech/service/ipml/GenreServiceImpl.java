package com.aptech.service.ipml;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aptech.constant.AppError;
import com.aptech.domain.AppServiceResult;
import com.aptech.dto.genre.GenreDto;
import com.aptech.entity.Genre;
import com.aptech.repository.GenreRepository;
import com.aptech.service.IGenreService;

@Service
public class GenreServiceImpl implements IGenreService {

	private final Logger logger = LoggerFactory.getLogger(GenreServiceImpl.class);

	private GenreRepository genreRepository;

	@Autowired
	public GenreServiceImpl(GenreRepository genreRepository) {
		this.genreRepository = genreRepository;
	}

	@Override
	public AppServiceResult<Iterable<GenreDto>> getGenres() {
		try {
			Iterable<Genre> genrers = genreRepository.findAll();

			List<GenreDto> genrerDtos = new ArrayList<GenreDto>();
			genrers.forEach((g) -> genrerDtos.add(new GenreDto(g.getId(), g.getName(), g.getDescription())));

			return new AppServiceResult<Iterable<GenreDto>>(true, 0, "", (Iterable<GenreDto>) genrerDtos);

		} catch (Exception e) {
			e.printStackTrace();

			logger.error(e.getMessage());

			return new AppServiceResult<Iterable<GenreDto>>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}
}
