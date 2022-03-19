package com.luizvaldiero.calculator.component;

import java.util.LinkedList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.luizvaldiero.calculator.enums.TokenType;
import com.luizvaldiero.calculator.model.Token;

@Component
public class ShuntingYardAlgorithm {
//	https://pt.stackoverflow.com/questions/6382/receber-uma-express%C3%A3o-e-calcular-em-c
// transform infix notation to postfix notation
		
	public List<Token> execute(List<Token> tokens) {
		List<Token> output = new LinkedList<>();
		List<Token> queue = new LinkedList<>();
		
		
		tokens.forEach((Token token) -> {
			if (TokenType.NUMBER.equals(token.getType())) {
				output.add(token);
			} else {
				if (!queue.isEmpty() && queue.get(0).hasGreaterPrecedenceThan(token)) {
					output.addAll(queue);
					queue.clear();
				}
				queue.add(0, token);
			}
		});		
		output.addAll(queue);
		
		return output;
	}
}
