package com.luizvaldiero.calculator.component;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.luizvaldiero.calculator.enums.TokenType;
import com.luizvaldiero.calculator.model.Token;

@ExtendWith(SpringExtension.class)
class BreakExpressionTest {
	
	@Test
	@DisplayName("return tokens When break simples expression")
	void returnToken_WhenBreakSimplesExpression() {

		String expression = "-3.+2.533";
		List<Token> expected = List.of(
				new Token("-3.", TokenType.NUMBER, 0),
				new Token("+", TokenType.BINARY_OPERATORS, 0),
				new Token("2.533", TokenType.NUMBER, 0)
		);
		
		BreakExpression breakExpression = new BreakExpression();
		
		List<Token> tokens = assertDoesNotThrow(() -> breakExpression.execute(expression));
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
				new Token("-3.", TokenType.NUMBER, 0),
				new Token("/", TokenType.BINARY_OPERATORS, 0),
				new Token("-2.533", TokenType.NUMBER, 0)
		);
		
		BreakExpression breakExpression = new BreakExpression();
		List<Token> tokens = assertDoesNotThrow(() -> breakExpression.execute(expression));
		
		assertThat(tokens.size()).isEqualTo(expected.size());
		
		for (int i = 0; i < expected.size(); i++) {
			Token tokenExpected = expected.get(i);
			assertThat(tokens.get(i).getValue()).isEqualTo(tokenExpected.getValue());
			assertThat(tokens.get(i).getType()).isEqualTo(tokenExpected.getType());
		}
	}
	
	@Test
	@DisplayName("throws if expression contains invalid token")
	void throws_ifExpressionContainsInvalidToken() {
		String expression = "-3./j-2.533";
		String expectedExceptionMessage = "invalid token: 'j' between 4...4";
		String expression2 = "-3./-j2.533";
		String expectedExceptionMessage2 = "invalid token: '-j2.533' between 4...10";

		BreakExpression breakExpression = new BreakExpression();
		
		Assertions.assertThatThrownBy(() -> breakExpression.execute(expression))
			.isInstanceOf(RuntimeException.class)
			.hasMessage(expectedExceptionMessage);
		
		Assertions.assertThatThrownBy(() -> breakExpression.execute(expression2))
			.isInstanceOf(RuntimeException.class)
			.hasMessage(expectedExceptionMessage2);
	}

}
