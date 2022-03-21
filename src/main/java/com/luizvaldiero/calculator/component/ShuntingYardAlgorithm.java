package com.luizvaldiero.calculator.component;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.luizvaldiero.calculator.enums.TokenType;
import com.luizvaldiero.calculator.model.Token;

@Component
public class ShuntingYardAlgorithm {

	public List<Token> transformToPostFixNotation(List<Token> tokensFromInFixNotation) {
		List<Token> output = new LinkedList<>();
		Deque<Token> stack = new LinkedList<>();
		
		tokensFromInFixNotation.forEach((Token token) -> {
			if (TokenType.NUMBER == token.getType()) {
				output.add(token);
			} else {
				if (canNotStack(stack, token)) {
					output.addAll(stack);
					stack.clear();
				}
				stack.addFirst(token);
			}
		});		
		output.addAll(stack);
		
		return output;
	}
	
	private boolean canNotStack(Deque<Token> stack, Token token) {
		return !stack.isEmpty() && stack.getFirst().hasGreaterPrecedenceThan(token);
	}
}
