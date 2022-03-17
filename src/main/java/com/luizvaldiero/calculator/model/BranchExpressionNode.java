package com.luizvaldiero.calculator.model;

import java.math.BigDecimal;
import java.util.function.BiFunction;

public class BranchExpressionNode extends ExpressionNode {
	private BiFunction<BigDecimal, BigDecimal, BigDecimal> calculator;
	
	public BranchExpressionNode(BiFunction<BigDecimal, BigDecimal, BigDecimal> calculator) {
		this.calculator = calculator;
	}

	@Override
	public BigDecimal calculate() {
		return calculator.apply(left.calculate(), right.calculate());
	}

}
