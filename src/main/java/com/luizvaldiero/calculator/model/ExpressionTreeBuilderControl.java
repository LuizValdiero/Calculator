package com.luizvaldiero.calculator.model;

import java.util.List;

public class ExpressionTreeBuilderControl {
	List<Token> tokens;
	Integer index;
	Integer size;
	public ExpressionTreeBuilderControl(List<Token> tokens) {
		super();
		this.index = 0;
		this.size = tokens.size();
		this.tokens = tokens;
	}

	public Boolean isFinished() {
		return index >= size;
	}
	
	public Token getNextToken() {
		return tokens.get(index++);
	}
}