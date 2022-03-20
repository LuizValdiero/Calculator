package com.luizvaldiero.calculator.exception;

public class ExceptionDetails {
	Integer statusCode;
	String error;
	
	public ExceptionDetails() {
		statusCode = 500;
		error = "undefined";
	}
	
	public ExceptionDetails(String error, Integer statusCode) {
		this.error = error;
		this.statusCode = statusCode;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public Integer getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(Integer httpStatus) {
		this.statusCode = httpStatus;
	}
}
