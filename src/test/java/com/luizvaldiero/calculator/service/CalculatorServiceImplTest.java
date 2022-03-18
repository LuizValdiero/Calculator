package com.luizvaldiero.calculator.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.luizvaldiero.calculator.component.BreakExpression;
import com.luizvaldiero.calculator.component.ExpressionTreeBuilder;
import com.luizvaldiero.calculator.dto.CalculatorRequestDTO;
import com.luizvaldiero.calculator.dto.CalculatorResposeDTO;
import com.luizvaldiero.calculator.enums.TokenType;
import com.luizvaldiero.calculator.model.ExpressionNode;
import com.luizvaldiero.calculator.model.Token;

@ExtendWith(SpringExtension.class)
class CalculatorServiceImplTest {

	@InjectMocks
	CalculatorServiceImpl calculatorServiceImpl;
	
	@Mock private BreakExpression breakExpression;
	@Mock private ExpressionTreeBuilder expressionTreeBuilder;

	private CalculatorRequestDTO inputDTO = new CalculatorRequestDTO("1.123");
	private ExpressionNode rootNodeFake = mock(ExpressionNode.class);
	
	@BeforeEach
	void setup() {
		
		BDDMockito.when(rootNodeFake.calculate()).thenReturn(BigDecimal.valueOf(1.123));
		BDDMockito.when(breakExpression.execute(ArgumentMatchers.anyString()))
			.thenReturn(List.of());
		BDDMockito.when(expressionTreeBuilder.create(ArgumentMatchers.anyList()))
			.thenReturn(rootNodeFake);
	}
	
	@Test
	@DisplayName("call breakExpression with expression params")
	void callBreakExpression_WithExpressionParams() {
		List<Token> tokens = List.of(new Token("1.123", TokenType.NUMBER));
		BigDecimal expectedResult = BigDecimal.valueOf(1.12).setScale(2, RoundingMode.UP);
		
		BDDMockito.when(rootNodeFake.calculate()).thenReturn(BigDecimal.valueOf(1.123));
		BDDMockito.when(breakExpression.execute(ArgumentMatchers.anyString()))
			.thenReturn(tokens);
		BDDMockito.when(expressionTreeBuilder.create(ArgumentMatchers.anyList()))
		.thenReturn(rootNodeFake);
		
		CalculatorResposeDTO resultDto = calculatorServiceImpl.calculate(inputDTO);

		BDDMockito.verify(breakExpression, times(1)).execute(inputDTO.getExpressao());
		BDDMockito.verify(expressionTreeBuilder, times(1)).create(tokens);
		
		assertThat(resultDto.getResultado()).isEqualTo(expectedResult);
	}
	
	@Test
	@DisplayName("Throws If the expression is malformed")
	void throws_malformed_expression() {

		List<String> expressions = List.of(
				"", " ", "--10", "++10", "*10", "/10",
				"2++1", "2--1", "2//+1", "2//1",
				"2**2", "2*/2", "2/*2", "10.0.2",
				"20.0++1", "20.0.3+1", "20.0.3+1",
				"20.0.3+1", "2*2.0.0", "2--2.0",
				"2++2.0", "2**2.0", "2//2.0"
		);
		for(String expression: expressions) {
			CalculatorRequestDTO request = new CalculatorRequestDTO(expression);
			
			Assertions.assertThatThrownBy(() -> calculatorServiceImpl.calculate(request))
			.isInstanceOf(RuntimeException.class)
			.hasMessage("Expressão mal formada");
		}
	}
	
	@Test
	@DisplayName("Not throws If the expression is perfect")
	void notThrows_perfect_expression() {

		List<String> expressions = List.of(
				"1", "10", "10.2", "+10.2", "-10.2",
				"1.", "1.+1.", "2+3", "2-3", "2*3",
				"2/3", "10/3", "2+-3", "2-+3",
				"2*-3", "2*+3", "2/-3",
				"2/+3", "-2.3243-2.43",
				"-98*-2.44+32/5.00+76*3/1.5"
		);
		for(String expression: expressions) {
			CalculatorRequestDTO request = new CalculatorRequestDTO(expression);
			
			assertDoesNotThrow(() -> calculatorServiceImpl.calculate(request));
		}
	}
}
