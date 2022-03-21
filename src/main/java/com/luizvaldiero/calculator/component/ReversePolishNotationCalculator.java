package com.luizvaldiero.calculator.component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiFunction;

import org.springframework.stereotype.Component;

import com.luizvaldiero.calculator.enums.TokenType;
import com.luizvaldiero.calculator.exception.InvalidExpressionException;
import com.luizvaldiero.calculator.model.Token;

@Component
public class ReversePolishNotationCalculator {
	
	public BigDecimal calculateInfixNotation(List<Token> tokens) {
		Deque<BigDecimal> stack = new LinkedList<>();
		
		tokens.forEach((Token token) -> {
			if (TokenType.NUMBER.equals(token.getType())) {
				BigDecimal number = new BigDecimal(token.getValue()); 
				stack.addLast(number);
			} else {
				BigDecimal right = stack.removeLast();
				BigDecimal left = stack.removeLast();
				BiFunction<BigDecimal, BigDecimal, BigDecimal> function = operatorsFactoryGetFunction(token.getValue());
				BigDecimal result = function.apply(left, right);
				stack.addLast(result);
			}
		});
		
		return stack.removeLast();
	}
	
	private BiFunction<BigDecimal, BigDecimal, BigDecimal> operatorsFactoryGetFunction(String operator) {
		switch (operator) {
		case "+":
			return (BigDecimal a, BigDecimal b) -> a.add(b);
		case "-":
			return (BigDecimal a, BigDecimal b) -> a.subtract(b);
		case "*":
			return (BigDecimal a, BigDecimal b) -> a.multiply(b);
		case "/":
			return (BigDecimal a, BigDecimal b) -> a.divide(b, RoundingMode.UP);
		default:
			throw new InvalidExpressionException("Invalid Operator '" + operator + "'");
		}
	}
}
