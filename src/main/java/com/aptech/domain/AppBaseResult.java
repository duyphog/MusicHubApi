package com.aptech.domain;

public class AppBaseResult {
	private boolean success;
	private int errorCode;
	private String errorMessage;

	public AppBaseResult() {
	}

	public AppBaseResult(boolean success, int errorCode, String errorMessage) {
		this.success = success;
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	public boolean isSuccess() {
		return success;
	}

	protected void setSuccess(boolean success) {
		this.success = success;
	}

	public int getErrorCode() {
		return errorCode;
	}

	protected void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	protected void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
