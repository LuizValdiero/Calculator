package com.luizvaldiero.calculator.model;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class BranchExpressionNodeTest {

	// TODO more tests
	@Test
	@DisplayName("throws if divide by zero")
	void throws_ifDivideByZero() {
		ExpressionNode left = new ExpressionNode() {
			
			@Override
			public BigDecimal calculate() {
				return BigDecimal.valueOf(1);
			}
		};
		ExpressionNode rigth = new ExpressionNode() {
			
			@Override
			public BigDecimal calculate() {
				return BigDecimal.valueOf(0);
			}
		};
		
		ExpressionNode sut = new BranchExpressionNode((a, b) -> a.divide(b));
		sut.setLeft(left);
		sut.setRight(rigth);
		
		Assertions.assertThatThrownBy(() -> sut.calculate())
		.isInstanceOf(ArithmeticException.class)
		.hasMessage("Division by zero");
	}

	@Test
	@DisplayName("CHANGE TEST RENAME")
	void notThros_OTHERS_NAME() {
		ExpressionNode left = new ExpressionNode() {
			
			@Override
			public BigDecimal calculate() {
				return new BigDecimal("2");
			}
		};
		ExpressionNode rigth = new ExpressionNode() {
			
			@Override
			public BigDecimal calculate() {
				return new BigDecimal("3");
			}
		};

		
		ExpressionNode sut = new BranchExpressionNode((a, b) -> a.divide(b, RoundingMode.UP));
		sut.setLeft(left);
		sut.setRight(rigth);
		
		assertDoesNotThrow(() -> sut.calculate());
	}
}
