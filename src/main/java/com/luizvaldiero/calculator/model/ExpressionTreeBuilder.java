package com.luizvaldiero.calculator.model;

import java.math.BigDecimal;
import java.util.List;

import com.luizvaldiero.calculator.enums.TokenType;

public class ExpressionTreeBuilder {
	public class ExpressionTreeBuilderControl {
		List<Token> tokens;
		Integer index;
		Integer size;
		public ExpressionTreeBuilderControl(List<Token> tokens) {
			super();
			this.index = 0;
			this.size = tokens.size();
			this.tokens = tokens;
		}

		public Boolean isFinished() {
			return index >= size;
		}
		
		public Token getNextToken() {
			return tokens.get(index++);
		}
	}
	
	public ExpressionNode create(List<Token> tokens) {
		ExpressionTreeBuilderControl control = new ExpressionTreeBuilderControl(tokens);
		
		Token firstToken = control.getNextToken();
		ExpressionNode root = new LeafExpressionNode(firstToken);
		
		root = this.createNextNode(control, root);
		
		return root;
	}
	
	public ExpressionNode createNextNode(ExpressionTreeBuilderControl control, ExpressionNode root) {
		if (control.isFinished()) {
			return root;
		}
		Token nextToken = control.getNextToken();
		
		if (TokenType.NUMBER.equals(nextToken.getType())) {
			ExpressionNode leaf = new LeafExpressionNode(nextToken);
			return leaf;
		}
		if (TokenType.ADDITION.equals(nextToken.getType())) {
			ExpressionNode expression = new BranchExpressionNode((BigDecimal a, BigDecimal b) -> a.add(b));
			expression.setLeft(root);
			expression.setRight(createNextNode(control, expression));
			return expression;
		}
		if (TokenType.SUBTRACTION.equals(nextToken.getType())) {
			ExpressionNode expression = new BranchExpressionNode((BigDecimal a, BigDecimal b) -> a.subtract(b));
			expression.setLeft(root);
			expression.setRight(createNextNode(control, createNextNode(control, expression)));
			return expression;
		}
		if (TokenType.MULTIPLICATION.equals(nextToken.getType())) {
			ExpressionNode expression = new BranchExpressionNode((BigDecimal a, BigDecimal b) -> a.multiply(b));
			expression.setLeft(root);
			expression.setRight(createNextNode(control, createNextNode(control, expression)));
			return createNextNode(control, expression);
		}
		if (TokenType.DIVISION.equals(nextToken.getType())) {
			ExpressionNode expression = new BranchExpressionNode((BigDecimal a, BigDecimal b) -> a.divide(b));
			expression.setLeft(root);
			expression.setRight(createNextNode(control, expression));
			return createNextNode(control, expression);
		}
		throw new IllegalStateException("Valor inesperado: " + nextToken.getValue());
	}

}
