package com.luizvaldiero.calculator.model;

import com.luizvaldiero.calculator.enums.TokenType;

public class Token {
	private String value;
	private TokenType type;
	private Integer precedence;

	public Token(String value, TokenType type, Integer precedence) {
		this.value = value;
		this.type = type;
		this.precedence = precedence;
	}
	
	public boolean hasGreaterPrecedenceThan(Token other) {
		return this.precedence.compareTo(other.getPrecedence()) < 0;
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
	
	public Integer getPrecedence() {
		return precedence;
	}
	
	public void setPrecedence(Integer precedence) {
		this.precedence = precedence;
	}
}
