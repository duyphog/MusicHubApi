package com.aptech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptech.domain.AppServiceResult;
import com.aptech.dto.GenreDto;
import com.aptech.dto.HttpResponse;
import com.aptech.dto.HttpResponseError;
import com.aptech.dto.HttpResponseSuccess;
import com.aptech.service.IGenreService;

@RestController
@RequestMapping("/genre")
public class GenreController {
	
	private IGenreService genreService;
	
	@Autowired
	public GenreController(IGenreService genreService) {
		this.genreService = genreService;
	}
	
	@GetMapping
	public ResponseEntity<HttpResponse> getGenres() {

		AppServiceResult<Iterable<GenreDto>> result = genreService.getGenres();

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<Iterable<GenreDto>>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
}
