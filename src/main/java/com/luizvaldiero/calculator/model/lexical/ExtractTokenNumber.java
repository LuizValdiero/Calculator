package com.luizvaldiero.calculator.model.lexical;

import org.springframework.data.util.Pair;

import com.luizvaldiero.calculator.enums.TokenType;
import com.luizvaldiero.calculator.model.Token;

public class ExtractTokenNumber extends ExtractToken {
	private static final String NUMBER = "^([\\+-]?\\d+\\.?\\d*)$";
	private static final String FIRST_CHARACTER = "(\\+|-|\\d)";
	private static final Integer PRECEDENCE = 0;

	@Override
	public Pair<Integer, Token> extract(String expression, String character, TokenType lastTokenType, Integer index) {
		if (lastTokenType == TokenType.NUMBER || !character.matches(FIRST_CHARACTER)) {
			return this.next(expression, character, lastTokenType, index);
		}

		int end = index+1;
		Integer length = expression.length();
		
		while (end < length && expression.substring(index, end+1).matches(NUMBER)) end++;
		String tokenString = expression.substring(index, end);
		
		if (!tokenString.matches(NUMBER)) {
			return this.next(expression, character, lastTokenType, index);
		}
		
		Token token = new Token(tokenString, TokenType.NUMBER, PRECEDENCE);
		
		return Pair.of(end, token);
	}
}
