package com.luizvaldiero.calculator.model.lexical;

import org.springframework.data.util.Pair;

import com.luizvaldiero.calculator.model.Token;

public abstract class ExtractToken {
	private ExtractToken nextExtractToken;
	
	public abstract Pair<Integer, Token> extract(String expression, String character, Integer index);

	public final ExtractToken setNextExtracToken(ExtractToken nextExtractToken) {
		this.nextExtractToken = nextExtractToken;
		return nextExtractToken;
	}
	
	protected final Pair<Integer, Token> next(String expression, String character, Integer index) {
		return nextExtractToken.extract(expression, character, index);
	}
}
