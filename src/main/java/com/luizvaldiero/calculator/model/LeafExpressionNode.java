package com.luizvaldiero.calculator.model;

import java.math.BigDecimal;

public class LeafExpressionNode extends ExpressionNode {
	
	private Token token;

	public LeafExpressionNode(Token token) {
		this.token = token;
	}
	
	@Override
	public BigDecimal calculate() {
		return new BigDecimal(token.getValue());
	}

}
