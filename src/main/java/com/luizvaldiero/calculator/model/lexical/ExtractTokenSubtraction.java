package com.luizvaldiero.calculator.model.lexical;

import org.springframework.data.util.Pair;

import com.luizvaldiero.calculator.enums.TokenType;
import com.luizvaldiero.calculator.model.Token;

public class ExtractTokenSubtraction extends ExtractToken {
	private static final String SUBTRACTION = "^-$";
	private static final Integer PRECEDENCE = 2;

	@Override
	public Pair<Integer, Token> extract(String expression, String character, TokenType lastTokenType, Integer index) {
		if (isValidToken(expression, character, lastTokenType, index)) {
			Token token = new Token("-", TokenType.BINARY_OPERATORS, PRECEDENCE);
			return Pair.of(index+1, token);
		}
		return next(expression, character, lastTokenType, index);
	}

	private Boolean isValidToken(String expression, String character,
			TokenType lastTokenType, Integer index) {
		return character.matches(SUBTRACTION) &&
				lastTokenType != TokenType.BINARY_OPERATORS &&
				index > 0 &&
				isNotTheLastCharacter(expression, index);				
	}
}
