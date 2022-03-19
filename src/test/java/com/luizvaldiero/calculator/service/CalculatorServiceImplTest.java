package com.luizvaldiero.calculator.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.times;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

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
import com.luizvaldiero.calculator.component.ReversePolishNotationCalculator;
import com.luizvaldiero.calculator.component.ShuntingYardAlgorithm;
import com.luizvaldiero.calculator.dto.CalculatorRequestDTO;
import com.luizvaldiero.calculator.dto.CalculatorResposeDTO;
import com.luizvaldiero.calculator.enums.TokenType;
import com.luizvaldiero.calculator.model.ResultModel;
import com.luizvaldiero.calculator.model.Token;
import com.luizvaldiero.calculator.repository.ResultModelRepository;

@ExtendWith(SpringExtension.class)
class CalculatorServiceImplTest {

	@InjectMocks
	CalculatorServiceImpl calculatorServiceImpl;
	
	@Mock private ResultModelRepository resultModelRepository;
	@Mock private BreakExpression breakExpression;
	@Mock private ShuntingYardAlgorithm shuntingYardAlgorithm;
	@Mock private ReversePolishNotationCalculator reversePolishNotationCalculator;

	private CalculatorRequestDTO inputDTO = new CalculatorRequestDTO("1.123");
	
	@BeforeEach
	void setup() {

		BDDMockito.when(resultModelRepository.findByExpression(ArgumentMatchers.anyString()))
			.thenReturn(Optional.empty());

		BDDMockito.when(resultModelRepository.save(ArgumentMatchers.any(ResultModel.class)))
			.thenReturn(new ResultModel("1", BigDecimal.valueOf(1)));

		BDDMockito.when(breakExpression.execute(ArgumentMatchers.anyString()))
			.thenReturn(List.of());

		BDDMockito.when(shuntingYardAlgorithm.execute(ArgumentMatchers.anyList()))
			.thenReturn(List.of());

		BDDMockito.when(reversePolishNotationCalculator.calculateInfixNotation(ArgumentMatchers.anyList()))
			.thenReturn(BigDecimal.valueOf(1));
	}
	
	@Test
	@DisplayName("return result When calculate valid expression")
	void returnResult_whenCalculateValidExpression() {
		List<Token> tokens = List.of(new Token("1.123", TokenType.NUMBER, 0));
		List<Token> tokensInPostFix = List.of(new Token("1.123", TokenType.NUMBER, 0));

		BigDecimal expectedResult = BigDecimal.valueOf(1.123).setScale(2, RoundingMode.UP);
		
		
		
		BDDMockito.when(breakExpression.execute(ArgumentMatchers.anyString()))
			.thenReturn(tokens);
		BDDMockito.when(shuntingYardAlgorithm.execute(ArgumentMatchers.anyList()))
			.thenReturn(tokensInPostFix);

		BDDMockito.when(reversePolishNotationCalculator.calculateInfixNotation(ArgumentMatchers.anyList()))
			.thenReturn(BigDecimal.valueOf(1.123));
				
		CalculatorResposeDTO resultDto = calculatorServiceImpl.calculate(inputDTO);

		BDDMockito.verify(resultModelRepository, times(1)).findByExpression(inputDTO.getExpressao());
		BDDMockito.verify(breakExpression, times(1)).execute(inputDTO.getExpressao());
		BDDMockito.verify(shuntingYardAlgorithm, times(1)).execute(tokens);
		BDDMockito.verify(reversePolishNotationCalculator, times(1)).calculateInfixNotation(tokensInPostFix);
		BDDMockito.verify(resultModelRepository, times(1)).save(ArgumentMatchers.any(ResultModel.class));
		
		assertThat(resultDto.getResultado()).isEqualTo(expectedResult);
	}

	
	@Test
	@DisplayName("return result When restoring the calculated expression")
	void returnResult_whenRestoringTheCalculatedExpression() {
		BigDecimal expectedResult = BigDecimal.valueOf(1.123);
		ResultModel resultModel = new ResultModel("1.123", BigDecimal.valueOf(1.123));
		
		BDDMockito.when(resultModelRepository.findByExpression(ArgumentMatchers.anyString()))
			.thenReturn(Optional.of(resultModel));
		
		CalculatorResposeDTO resultDto = calculatorServiceImpl.calculate(inputDTO);

		BDDMockito.verify(resultModelRepository, times(1)).findByExpression("1.123");
		BDDMockito.verify(breakExpression, times(0)).execute(ArgumentMatchers.anyString());
		BDDMockito.verify(shuntingYardAlgorithm, times(0)).execute(ArgumentMatchers.anyList());
		BDDMockito.verify(reversePolishNotationCalculator, times(0)).calculateInfixNotation(ArgumentMatchers.anyList());
		BDDMockito.verify(resultModelRepository, times(0)).save(ArgumentMatchers.any());
		
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
