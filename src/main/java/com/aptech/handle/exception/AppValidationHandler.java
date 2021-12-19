package com.aptech.handle.exception;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.aptech.domain.HttpResponse;
import com.aptech.domain.HttpResponseError;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestControllerAdvice
public class AppValidationHandler extends ResponseEntityExceptionHandler {

	private final Logger logger = LogManager.getLogger(AppValidationHandler.class);

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {

			String fieldName = ((FieldError) error).getField();
			String message = error.getDefaultMessage();
			errors.put(fieldName, message);
		});

		HttpStatus httpStatusRes = HttpStatus.BAD_REQUEST;

		String jsonErrors;
		try {
			jsonErrors = new ObjectMapper().writeValueAsString(errors);
		} catch (JsonProcessingException e) {
			logger.error(e.getMessage());
			
			jsonErrors = "Invalid data";
		}

		HttpResponse httpResponse = new HttpResponseError(httpStatusRes, httpStatusRes.getReasonPhrase(), jsonErrors);

		return new ResponseEntity<Object>(httpResponse, httpStatusRes);
	}
}
