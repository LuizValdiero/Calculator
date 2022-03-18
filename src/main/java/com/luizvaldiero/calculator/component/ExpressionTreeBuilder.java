package com.luizvaldiero.calculator.component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.springframework.stereotype.Component;

import com.luizvaldiero.calculator.enums.TokenType;
import com.luizvaldiero.calculator.model.BranchExpressionNode;
import com.luizvaldiero.calculator.model.ExpressionNode;
import com.luizvaldiero.calculator.model.ExpressionTreeBuilderControl;
import com.luizvaldiero.calculator.model.LeafExpressionNode;
import com.luizvaldiero.calculator.model.Token;

@Component
public class ExpressionTreeBuilder {
	
	public ExpressionNode create(List<Token> tokens) {
		ExpressionTreeBuilderControl control = new ExpressionTreeBuilderControl(tokens);
		
		Token firstToken = control.getNextToken();
		ExpressionNode root = new LeafExpressionNode(firstToken);
		
		root = this.createNextNode(control, root);
		
		return root;
	}
	
	private ExpressionNode createNextNode(ExpressionTreeBuilderControl control, ExpressionNode root) {
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
			ExpressionNode expression = new BranchExpressionNode((BigDecimal a, BigDecimal b) -> a.divide(b, RoundingMode.UP));
			expression.setLeft(root);
			expression.setRight(createNextNode(control, expression));
			return createNextNode(control, expression);
		}
		throw new IllegalStateException("Valor inesperado: " + nextToken.getValue());
	}

}
