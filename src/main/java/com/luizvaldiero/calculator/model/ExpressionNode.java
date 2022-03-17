package com.luizvaldiero.calculator.model;

import java.math.BigDecimal;

public abstract class ExpressionNode {
	ExpressionNode root;
	ExpressionNode left;
	ExpressionNode right;
	
	public abstract BigDecimal calculate();

	public ExpressionNode getRoot() {
		return root;
	}

	public void setRoot(ExpressionNode root) {
		this.root = root;
	}

	public ExpressionNode getLeft() {
		return left;
	}

	public void setLeft(ExpressionNode left) {
		this.left = left;
	}

	public ExpressionNode getRight() {
		return right;
	}

	public void setRight(ExpressionNode right) {
		this.right = right;
	}
}
