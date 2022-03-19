package com.luizvaldiero.calculator.exception;

public class InvalidExpressionException extends RuntimeException {
	private static final long serialVersionUID = 3860838919018701305L;

	public InvalidExpressionException(String message) {
		super(message);
	}
}
