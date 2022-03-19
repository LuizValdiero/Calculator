package com.luizvaldiero.calculator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler {
	
	@ExceptionHandler({ArithmeticException.class, InvalidExpressionException.class})
	public ResponseEntity<ExceptionDetails> handlerArithmeticException(RuntimeException exception) {
		ExceptionDetails body = new ExceptionDetails(exception.getMessage(), HttpStatus.BAD_REQUEST.value());
		return ResponseEntity.badRequest().body(body);
	}

}
