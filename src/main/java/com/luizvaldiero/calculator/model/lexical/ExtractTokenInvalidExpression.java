package com.luizvaldiero.calculator.model.lexical;

import org.springframework.data.util.Pair;

import com.luizvaldiero.calculator.exception.InvalidExpressionException;
import com.luizvaldiero.calculator.model.Token;

public class ExtractTokenInvalidExpression extends ExtractToken {

	@Override
	public Pair<Integer, Token> extract(String expression, String character, Integer index) {
		throw new InvalidExpressionException("invalid token (line " + index + "): '" + character + "'");
	}
}
