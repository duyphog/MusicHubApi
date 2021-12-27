package com.aptech.dto;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HttpResponseError extends HttpResponse {

	private String reason;

	private String errorMessage;

	public HttpResponseError(String reason, String errorMessage) {
		super(Boolean.FALSE, HttpStatus.BAD_REQUEST);

		this.reason = reason;
		this.errorMessage = errorMessage;
	}

	public HttpResponseError(HttpStatus httpStatus, String reason, String errorMessage) {
		super(Boolean.FALSE, httpStatus);

		this.reason = reason;
		this.errorMessage = errorMessage;
	}
}