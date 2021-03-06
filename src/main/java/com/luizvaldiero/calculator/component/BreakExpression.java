package com.luizvaldiero.calculator.component;

import java.util.LinkedList;
import java.util.List;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import com.luizvaldiero.calculator.enums.TokenType;
import com.luizvaldiero.calculator.model.Token;
import com.luizvaldiero.calculator.model.lexical.ExtractToken;

@Component
public class BreakExpression {
	private ExtractToken extractToken;

	public void setExtractToken(ExtractToken extractToken) {
		this.extractToken = extractToken;
	}

	public List<Token> execute(String expression) {
		List<Token> tokens = new LinkedList<>();
		Integer index = 0;
		Integer length = expression.length();
		
		TokenType lastTokenType = null;
		while (index < length) {
			String character = expression.substring(index, index+1);
			Pair<Integer, Token> result = extractToken.extract(expression, character, lastTokenType, index);
			index = result.getFirst();
			Token token = result.getSecond();
			tokens.add(token);
			lastTokenType = token.getType();
		}
		return tokens;
	}
}