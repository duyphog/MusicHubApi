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
import com.aptech.service.GenreService;

@Service
public class GenreServiceImpl implements GenreService {

	private final Logger logger = LoggerFactory.getLogger(GenreServiceImpl.class);

	private GenreRepository genreRepository;

	@Autowired
	public GenreServiceImpl(GenreRepository genreRepository) {
		this.genreRepository = genreRepository;
	}

	@Override
	public AppServiceResult<List<GenreDto>> getGenres() {
		try {
			Iterable<Genre> genrers = genreRepository.findAll();

			List<GenreDto> genrerDtos = new ArrayList<GenreDto>();
			genrers.forEach((g) -> genrerDtos.add(new GenreDto(g.getId(), g.getName(), g.getDescription())));

			return new AppServiceResult<List<GenreDto>>(true, 0, "", genrerDtos);

		} catch (Exception e) {
			e.printStackTrace();

			logger.error(e.getMessage());

			return new AppServiceResult<List<GenreDto>>(false, AppError.Unknown.errorCode(),
					AppError.Unknown.errorMessage(), null);
		}
	}
}
