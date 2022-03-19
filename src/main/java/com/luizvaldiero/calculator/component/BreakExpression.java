package com.luizvaldiero.calculator.component;

import java.util.LinkedList;
import java.util.List;

import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;

import com.luizvaldiero.calculator.enums.TokenType;
import com.luizvaldiero.calculator.model.Token;

@Component
public class BreakExpression {
	private static final String OPERATORS = "^(\\+|-|\\*|/)$";
	
	public List<Token> execute(String expression) {
		List<Token> tokens = new LinkedList<>();
		int index = 0;
		int length = expression.length();
		
		while (index < length) {
			Pair<Integer, Token> result = getNextToken(expression, index, length);
			index = result.getFirst();
			tokens.add(result.getSecond());
		}
		return tokens;
	}
	
	public Pair<Integer, Token> getNextToken(String expression, int index, int length) {
		int end = index+1;
		String caracter = expression.substring(index, end);
		if ("*".equals(caracter)) {
			return Pair.of(end, new Token("*", TokenType.MULTIPLICATION, 1));
		}
		if ("/".equals(caracter)) {
			return Pair.of(end, new Token("/", TokenType.DIVISION, 1));
		}
		if ("(".equals(caracter)) {
			return Pair.of(end, new Token("(", TokenType.PARENTHESES_OPEN, 0));
		}
		if (")".equals(caracter)) {
			return Pair.of(end, new Token(")", TokenType.PARENTHESES_CLOSE, 0));
		}
		if ("+".equals(caracter)) {
			if(index > 0 && !expression.substring(index-1, index).matches(OPERATORS)) {
				return Pair.of(end, new Token("+", TokenType.ADDITION, 2));
			}
		}
		if ("-".equals(caracter)) {
			if(index > 0 && !expression.substring(index-1, index).matches(OPERATORS)) {
				return Pair.of(end, new Token("-", TokenType.SUBTRACTION, 2));
			}
		}
		while (end < length && !expression.substring(end, end+1).matches(OPERATORS)) end++;
		String token = expression.substring(index, end);
		return Pair.of(end, new Token(token, TokenType.NUMBER, 0));
	}
}