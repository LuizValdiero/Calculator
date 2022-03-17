package com.luizvaldiero.calculator.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.luizvaldiero.calculator.enums.TokenType;


@ExtendWith(SpringExtension.class)
class LeafExpressionNodeTest {

	// TODO more tests
	@Test
	@DisplayName("return value from token")
	void test() {
		String input = "-2.1";
		ExpressionNode expression = new LeafExpressionNode(new Token(input, TokenType.NUMBER));
		BigDecimal expected = new BigDecimal(input);
		
		assertThat(expression.calculate()).isEqualTo(expected);
	}

}
