package com.luizvaldiero.calculator.model.lexical;

import java.util.Optional;

import org.springframework.data.util.Pair;

import com.luizvaldiero.calculator.enums.TokenType;
import com.luizvaldiero.calculator.exception.InvalidExpressionException;
import com.luizvaldiero.calculator.model.Token;

public abstract class ExtractToken {
	
	private Optional<ExtractToken> nextExtractTokenOpt;

	public ExtractToken() {
		super();
		this.nextExtractTokenOpt = Optional.empty();
	}
	
	public abstract Pair<Integer, Token> extract(String expression, String character, TokenType lastTokenType, Integer index);

	public final ExtractToken setNextExtracToken(ExtractToken nextExtractToken) {
		this.nextExtractTokenOpt = Optional.of(nextExtractToken);
		return nextExtractToken;
	}
	
	protected final boolean isNotTheLastCharacter(String expression, Integer index) {
		Integer lastIndex = expression.length()-1;
		return index < lastIndex;
	}
	
	protected final Pair<Integer, Token> next(String expression, String character, TokenType lastTokenType, Integer index) {
		return nextExtractTokenOpt
				.orElseThrow(() -> new InvalidExpressionException("unexpected token (" + index + "): '" + character + "'"))
				.extract(expression, character, lastTokenType, index);
	}
}
