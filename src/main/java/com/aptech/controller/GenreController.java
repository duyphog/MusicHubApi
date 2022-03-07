package com.aptech.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.aptech.domain.AppServiceResult;
import com.aptech.dto.HttpResponse;
import com.aptech.dto.HttpResponseError;
import com.aptech.dto.HttpResponseSuccess;
import com.aptech.dto.genre.GenreDto;
import com.aptech.service.GenreService;

import java.util.List;

@RestController
@RequestMapping("/genre")
public class GenreController {
	
	private GenreService genreService;
	
	@Autowired
	public GenreController(GenreService genreService) {
		this.genreService = genreService;
	}
	
	@GetMapping
	public ResponseEntity<HttpResponse> getGenres() {

		AppServiceResult<List<GenreDto>> result = genreService.getGenres();

		return result.isSuccess() ? ResponseEntity.ok(new HttpResponseSuccess<Iterable<GenreDto>>(result.getData()))
				: ResponseEntity.badRequest().body(new HttpResponseError(null, result.getMessage()));
	}
}
