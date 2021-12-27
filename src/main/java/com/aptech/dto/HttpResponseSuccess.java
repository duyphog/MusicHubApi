package com.aptech.dto;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HttpResponseSuccess<T> extends HttpResponse {

	private T data;

	public HttpResponseSuccess(T data) {
		super(Boolean.TRUE, HttpStatus.OK);
		
		this.data = data;
	}
}