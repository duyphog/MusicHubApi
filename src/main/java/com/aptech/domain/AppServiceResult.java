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
		return super.isSuccess();
	}

	public void setSuccess(boolean success) {
		super.setSuccess(success);
	}

	public int getErrorCode() {
		return super.getErrorCode();
	}

	public void setErrorCode(int errorCode) {
		super.setErrorCode(errorCode);
	}

	public String getErrorMessage() {
		return super.getErrorMessage();
	}

	public void setErrorMessage(String errorMessage) {
		super.setErrorMessage(errorMessage);
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
