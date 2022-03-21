package com.luizvaldiero.calculator.component;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.luizvaldiero.calculator.enums.TokenType;
import com.luizvaldiero.calculator.model.Token;

@Component
public class ShuntingYardAlgorithm {

	public List<Token> transformToPostFixNotation(List<Token> tokensFromInFixNotation) {
		List<Token> output = new LinkedList<>();
		List<Token> stack = new LinkedList<>();
		
		tokensFromInFixNotation.forEach((Token token) -> {
			if (TokenType.NUMBER.equals(token.getType())) {
				output.add(token);
			} else {
				if (!stack.isEmpty() && stack.get(0).hasGreaterPrecedenceThan(token)) {
					output.addAll(stack);
					stack.clear();
				}
				stack.add(0, token);
			}
		});		
		output.addAll(stack);
		
		return output;
	}
}
