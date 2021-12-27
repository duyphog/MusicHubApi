package com.aptech.dto;

import java.util.Date;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class HttpResponse {

	private Boolean success;
	
	private HttpStatus httpStatus;
	
	private int httpStatusCode;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss", timezone = "Asia/Ho_Chi_Minh")
	private Date timestamp = new Date();

	public HttpResponse(boolean success, HttpStatus httpStatus) {
		this.success = success;
		this.httpStatus = httpStatus;
		this.httpStatusCode = httpStatus.value();
	}
}