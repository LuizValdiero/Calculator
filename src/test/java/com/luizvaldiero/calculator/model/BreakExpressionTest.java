package com.luizvaldiero.calculator.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.luizvaldiero.calculator.enums.TokenType;

@ExtendWith(SpringExtension.class)
class BreakExpressionTest {
	
	@Test
	@DisplayName("return tokens When break simples expression")
	void returnToken_WhenBreakSimplesExpression() {

		String expression = "-3.+2.533";
		List<Token> expected = List.of(
				new Token("-3.", TokenType.NUMBER),
				new Token("+", TokenType.ADDITION),
				new Token("2.533", TokenType.NUMBER)
		);
		
		BreakExpression breakExpression = new BreakExpression();
		List<Token> tokens = breakExpression.execute(expression);

		assertThat(tokens.size()).isEqualTo(expected.size());
		
		for (int i = 0; i < expected.size(); i++) {
			Token tokenExpected = expected.get(i);
			assertThat(tokens.get(i).getValue()).isEqualTo(tokenExpected.getValue());
			assertThat(tokens.get(i).getType()).isEqualTo(tokenExpected.getType());
		}
	}
	
	@Test
	@DisplayName("return tokens When break expression with operator and signal together")
	void returnTokens_WhenBreakExpressionWithOperatorAndSignalTogether() {

		String expression = "-3./-2.533";
		List<Token> expected = List.of(
				new Token("-3.", TokenType.NUMBER),
				new Token("/", TokenType.DIVISION),
				new Token("-2.533", TokenType.NUMBER)
		);
		
		BreakExpression breakExpression = new BreakExpression();
		List<Token> tokens = breakExpression.execute(expression);
		
		assertThat(tokens.size()).isEqualTo(expected.size());
		
		for (int i = 0; i < expected.size(); i++) {
			Token tokenExpected = expected.get(i);
			assertThat(tokens.get(i).getValue()).isEqualTo(tokenExpected.getValue());
			assertThat(tokens.get(i).getType()).isEqualTo(tokenExpected.getType());
		}
	}

}
