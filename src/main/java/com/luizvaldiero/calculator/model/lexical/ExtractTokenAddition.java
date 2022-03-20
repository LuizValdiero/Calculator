package com.luizvaldiero.calculator.model.lexical;

import org.springframework.data.util.Pair;

import com.luizvaldiero.calculator.enums.TokenType;
import com.luizvaldiero.calculator.model.Token;

public class ExtractTokenAddition extends ExtractToken {
	private static final String ADDITION = "^\\+$";
	private static final String PREVIOUS_VALUE = "^[\\d\\+\\.\\)]$";
	private static final Integer PRECEDENCE = 2;
	
	@Override
	public Pair<Integer, Token> extract(String expression, String character, Integer index) {
		if (character.matches(ADDITION)) {
			if(index > 0 && expression.substring(index-1, index).matches(PREVIOUS_VALUE)) {
				Token token = new Token("+", TokenType.BINARY_OPERATORS, PRECEDENCE);
				return Pair.of(index+1, token);
			}
		}
		return next(expression, character, index);
	}
}