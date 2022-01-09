package com.aptech.handle.exception;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.aptech.dto.HttpResponse;
import com.aptech.dto.HttpResponseError;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestControllerAdvice
public class AppExceptionHandler {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());
	
	private static final String ACCOUNT_LOCKED = "Your account has been locked. Please contact administration";
    private static final String METHOD_IS_NOT_ALLOWED = "This request method is not allowed on this endpoint. Please send a '%s' request";
    private static final String INTERNAL_SERVER_ERROR_MSG = "An error occurred while processing the request";
    private static final String INCORRECT_CREDENTIALS = "Username / password incorrect. Please try again";
    private static final String ACCOUNT_DISABLED = "Your account has been disabled. Please verify email or contact administration";
    private static final String ERROR_PROCESSING_FILE = "Error occurred while processing file";
    private static final String NOT_ENOUGH_PERMISSION = "You do not have enough permission";
	
	// Account
    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<HttpResponse> accountDisabledException() {
        return createHttpResponse(HttpStatus.BAD_REQUEST, ACCOUNT_DISABLED);
    }
    
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HttpResponse> badCredentialsException() {
        return createHttpResponse(HttpStatus.BAD_REQUEST, INCORRECT_CREDENTIALS);
    }
    
    @ExceptionHandler(LockedException.class)
    public ResponseEntity<HttpResponse> lockedException() {
        return createHttpResponse(HttpStatus.UNAUTHORIZED, ACCOUNT_LOCKED);
    }
    
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<HttpResponse> accessDeniedException() {
        return createHttpResponse(HttpStatus.FORBIDDEN, NOT_ENOUGH_PERMISSION);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HttpResponse> methodArgumentNotValidException(MethodArgumentNotValidException exception) {
    	
    	Map<String, String> errors = new HashMap<>();
    	exception.getBindingResult().getAllErrors().forEach((error) -> {

			String fieldName = ((FieldError) error).getField();
			String message = error.getDefaultMessage();
			errors.put(fieldName, message);
		});
		
    	String jsonErrors;
		try {
			jsonErrors = new ObjectMapper().writeValueAsString(errors);
		} catch (JsonProcessingException e) {
			LOGGER.error(e.getMessage());
			
			jsonErrors = "Invalid data";
		}
        return createHttpResponse(HttpStatus.BAD_REQUEST, jsonErrors);
    }
    
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<HttpResponse> methodNotSupportedException(HttpRequestMethodNotSupportedException exception) {
        HttpMethod supportedMethod = Objects.requireNonNull(exception.getSupportedHttpMethods()).iterator().next();
        return createHttpResponse(HttpStatus.METHOD_NOT_ALLOWED, String.format(METHOD_IS_NOT_ALLOWED, supportedMethod));
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpResponse> internalServerErrorException(Exception exception) {
        LOGGER.error(exception.getMessage());
        return createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MSG);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<HttpResponse> iOException(IOException exception) {
        LOGGER.error(exception.getMessage());
        return createHttpResponse(HttpStatus.INTERNAL_SERVER_ERROR, ERROR_PROCESSING_FILE);
    }
	private ResponseEntity<HttpResponse> createHttpResponse(HttpStatus httpStatus, String message) {
		HttpResponse httpResponse = new HttpResponseError(httpStatus, httpStatus.getReasonPhrase(), message);
		return new ResponseEntity<>(httpResponse, httpStatus);
	}
}
