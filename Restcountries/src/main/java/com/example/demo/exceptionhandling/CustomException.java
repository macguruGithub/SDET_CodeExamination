package com.example.demo.exceptionhandling;

public class CustomException extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5659534463932573494L;
	private String errorCode;
	private String errorMessage;
	public CustomException(String errorCode, String errorMessage) {
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	

}
