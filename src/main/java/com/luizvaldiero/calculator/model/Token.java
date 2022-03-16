package com.luizvaldiero.calculator.model;

import com.luizvaldiero.calculator.enums.TokenType;

public class Token {
	String value;
	TokenType type;

	public Token(String value, TokenType type) {
		this.value = value;
		this.type = type;
	}
	
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public TokenType getType() {
		return type;
	}
	public void setType(TokenType type) {
		this.type = type;
	}
}
