package com.aptech.domain;

import com.aptech.dto.BaseDto;

public final class AppServiceResult<T extends BaseDto> extends AppBaseResult {
	private T data;

	public AppServiceResult() {
	}

	public AppServiceResult(boolean success, int errorCode, String errorMessage, T data) {
		super(success, errorCode, errorMessage);
		this.data = data;
	}

	public boolean isSuccess() {
		return this.isSuccess();
	}

	public void setSuccess(boolean success) {
		this.setSuccess(success);
	}

	public int getErrorCode() {
		return this.getErrorCode();
	}

	public void setErrorCode(int errorCode) {
		this.setErrorCode(errorCode);
	}

	public String getErrorMessage() {
		return this.getErrorMessage();
	}

	public void setErrorMessage(String errorMessage) {
		this.setErrorMessage(errorMessage);
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
