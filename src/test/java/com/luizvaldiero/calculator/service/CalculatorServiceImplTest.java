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
import org.springframework.dao.DataIntegrityViolationException;
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

	@BeforeEach
	void setup() {

		BDDMockito.when(resultModelRepository.findByExpression(ArgumentMatchers.anyString()))
			.thenReturn(Optional.empty());

		BDDMockito.when(resultModelRepository.save(ArgumentMatchers.any(ResultModel.class)))
			.thenReturn(new ResultModel("1", BigDecimal.valueOf(1)));

		BDDMockito.when(breakExpression.execute(ArgumentMatchers.anyString()))
			.thenReturn(List.of());

		BDDMockito.when(shuntingYardAlgorithm.transformToPostFixNotation(ArgumentMatchers.anyList()))
			.thenReturn(List.of());

		BDDMockito.when(reversePolishNotationCalculator.calculatePostfixNotation(ArgumentMatchers.anyList()))
			.thenReturn(BigDecimal.valueOf(1));
	}
	
	@Test
	@DisplayName("returns result When calculate valid expression")
	void returnsResult_whenCalculateValidExpression() {
		CalculatorRequestDTO inputDTO = new CalculatorRequestDTO("1.123");
		List<Token> tokens = List.of(new Token("1.123", TokenType.NUMBER, 0));
		List<Token> tokensInPostFix = List.of(new Token("1.123", TokenType.NUMBER, 0));

		BigDecimal expectedResult = BigDecimal.valueOf(1.123).setScale(2, RoundingMode.UP);
				
		BDDMockito.when(breakExpression.execute(ArgumentMatchers.anyString()))
			.thenReturn(tokens);
		BDDMockito.when(shuntingYardAlgorithm.transformToPostFixNotation(ArgumentMatchers.anyList()))
			.thenReturn(tokensInPostFix);
		BDDMockito.when(reversePolishNotationCalculator.calculatePostfixNotation(ArgumentMatchers.anyList()))
			.thenReturn(BigDecimal.valueOf(1.123));
				
		CalculatorResposeDTO resultDto = calculatorServiceImpl.calculate(inputDTO);

		BDDMockito.verify(resultModelRepository, times(1)).findByExpression(inputDTO.getExpressao());
		BDDMockito.verify(breakExpression, times(1)).execute(inputDTO.getExpressao());
		BDDMockito.verify(shuntingYardAlgorithm, times(1)).transformToPostFixNotation(tokens);
		BDDMockito.verify(reversePolishNotationCalculator, times(1)).calculatePostfixNotation(tokensInPostFix);
		BDDMockito.verify(resultModelRepository, times(1)).save(ArgumentMatchers.any(ResultModel.class));
		
		assertThat(resultDto.getResultado()).isEqualTo(expectedResult);
	}
	
	@Test
	@DisplayName("returns result When restoring the calculated expression")
	void returnsResult_whenRestoringTheCalculatedExpression() {
		CalculatorRequestDTO inputDTO = new CalculatorRequestDTO("1.123");
		
		BigDecimal expectedResult = BigDecimal.valueOf(1.123);
		ResultModel resultModel = new ResultModel("1.123", BigDecimal.valueOf(1.123));
		
		BDDMockito.when(resultModelRepository.findByExpression(ArgumentMatchers.anyString()))
			.thenReturn(Optional.of(resultModel));
		
		CalculatorResposeDTO resultDto = calculatorServiceImpl.calculate(inputDTO);

		BDDMockito.verify(resultModelRepository, times(1)).findByExpression("1.123");
		BDDMockito.verify(breakExpression, times(0)).execute(ArgumentMatchers.anyString());
		BDDMockito.verify(shuntingYardAlgorithm, times(0)).transformToPostFixNotation(ArgumentMatchers.anyList());
		BDDMockito.verify(reversePolishNotationCalculator, times(0)).calculatePostfixNotation(ArgumentMatchers.anyList());
		BDDMockito.verify(resultModelRepository, times(0)).save(ArgumentMatchers.any());
		
		assertThat(resultDto.getResultado()).isEqualTo(expectedResult);
	}
	
	@Test
	@DisplayName("returns result Even if an error occurs to save")
	void returnsResult_evenIfAnErrorOccursToSave() {
		CalculatorRequestDTO inputDTO = new CalculatorRequestDTO("1.123");
		
		BigDecimal expectedResult = BigDecimal.valueOf(1.123);
		ResultModel resultModel = new ResultModel("1.123", BigDecimal.valueOf(1.123));
		
		BDDMockito.when(resultModelRepository.findByExpression(ArgumentMatchers.anyString()))
			.thenReturn(Optional.of(resultModel));
		BDDMockito.when(resultModelRepository.save(ArgumentMatchers.any(ResultModel.class)))
			.thenThrow(new DataIntegrityViolationException("value too long for column...."));

		CalculatorResposeDTO resultDto = assertDoesNotThrow(() -> calculatorServiceImpl.calculate(inputDTO));

		BDDMockito.verify(resultModelRepository, times(1)).findByExpression("1.123");
		BDDMockito.verify(breakExpression, times(0)).execute(ArgumentMatchers.anyString());
		BDDMockito.verify(shuntingYardAlgorithm, times(0)).transformToPostFixNotation(ArgumentMatchers.anyList());
		BDDMockito.verify(reversePolishNotationCalculator, times(0)).calculatePostfixNotation(ArgumentMatchers.anyList());
		BDDMockito.verify(resultModelRepository, times(0)).save(ArgumentMatchers.any());
		
		assertThat(resultDto.getResultado()).isEqualTo(expectedResult);
	}
	
	@Test
	@DisplayName("returns 4 If expression is equals to '2+2'")
	void returns4_ifExpressionIsEqualTo2Plus2() {
		CalculatorRequestDTO inputDTO = new CalculatorRequestDTO("2+2");
		List<Token> tokens = List.of(
				new Token("2", TokenType.NUMBER, 0),
				new Token("+", TokenType.BINARY_OPERATORS, 2),
				new Token("2", TokenType.NUMBER, 0));
		
		List<Token> tokensInPostFix = List.of(
				new Token("2", TokenType.NUMBER, 0),
				new Token("2", TokenType.NUMBER, 0),
				new Token("+", TokenType.BINARY_OPERATORS, 2));

		BigDecimal expectedResult = BigDecimal.valueOf(4);
		
		BDDMockito.when(breakExpression.execute(ArgumentMatchers.anyString()))
			.thenReturn(tokens);
		BDDMockito.when(shuntingYardAlgorithm.transformToPostFixNotation(ArgumentMatchers.anyList()))
			.thenReturn(tokensInPostFix);
		BDDMockito.when(reversePolishNotationCalculator.calculatePostfixNotation(ArgumentMatchers.anyList()))
			.thenReturn(BigDecimal.valueOf(4));

		CalculatorResposeDTO resultDto = assertDoesNotThrow(() -> calculatorServiceImpl.calculate(inputDTO));

		BDDMockito.verify(resultModelRepository, times(1)).findByExpression(inputDTO.getExpressao());
		BDDMockito.verify(breakExpression, times(1)).execute(inputDTO.getExpressao());
		BDDMockito.verify(shuntingYardAlgorithm, times(1)).transformToPostFixNotation(tokens);
		BDDMockito.verify(reversePolishNotationCalculator, times(1)).calculatePostfixNotation(tokensInPostFix);
		BDDMockito.verify(resultModelRepository, times(1)).save(ArgumentMatchers.any(ResultModel.class));		
		assertThat(resultDto.getResultado()).isEqualTo(expectedResult);
	}

	@Test
	@DisplayName("returns 4.4 If expression is equals to '2.2+2.2'")
	void returns4dot4_ifExpressionIsEqualTo2dot2Plus2dot2() {
		CalculatorRequestDTO inputDTO = new CalculatorRequestDTO("2.2+2.2");
		List<Token> tokens = List.of(
				new Token("2.2", TokenType.NUMBER, 0),
				new Token("+", TokenType.BINARY_OPERATORS, 2),
				new Token("2.2", TokenType.NUMBER, 0));

		List<Token> tokensInPostFix = List.of(
				new Token("2.2", TokenType.NUMBER, 0),
				new Token("2.2", TokenType.NUMBER, 0),
				new Token("+", TokenType.BINARY_OPERATORS, 2));
		
		ResultModel resultSaved = new ResultModel("2.2+2.2", BigDecimal.valueOf(4.4));
		
		BigDecimal expectedResult = BigDecimal.valueOf(4.4);
		
		BDDMockito.when(resultModelRepository.findByExpression(ArgumentMatchers.anyString()))
			.thenReturn(Optional.empty());
		
		BDDMockito.when(breakExpression.execute(ArgumentMatchers.anyString()))
			.thenReturn(tokens);
		BDDMockito.when(shuntingYardAlgorithm.transformToPostFixNotation(ArgumentMatchers.anyList()))
			.thenReturn(tokensInPostFix);
		BDDMockito.when(reversePolishNotationCalculator.calculatePostfixNotation(ArgumentMatchers.anyList()))
			.thenReturn(BigDecimal.valueOf(4.4));

		CalculatorResposeDTO resultDto = assertDoesNotThrow(() -> calculatorServiceImpl.calculate(inputDTO));
		assertThat(resultDto.getResultado()).isEqualTo(expectedResult);

		BDDMockito.when(resultModelRepository.findByExpression(ArgumentMatchers.anyString()))
			.thenReturn(Optional.of(resultSaved));
		
		CalculatorResposeDTO resultDto2 = assertDoesNotThrow(() -> calculatorServiceImpl.calculate(inputDTO));
		assertThat(resultDto2.getResultado()).isEqualTo(expectedResult);

		BDDMockito.verify(resultModelRepository, times(2)).findByExpression(inputDTO.getExpressao());
		BDDMockito.verify(breakExpression, times(1)).execute(inputDTO.getExpressao());
		BDDMockito.verify(shuntingYardAlgorithm, times(1)).transformToPostFixNotation(tokens);
		BDDMockito.verify(reversePolishNotationCalculator, times(1)).calculatePostfixNotation(tokensInPostFix);
		BDDMockito.verify(resultModelRepository, times(1)).save(ArgumentMatchers.any(ResultModel.class));		
	}

	@Test
	@DisplayName("returns 10.29 If expression is equals to '2.3*2.3+5'")
	void returns10dot29_ifExpressionIsEqualTo2dot3Multiply2dot3Plus5() {
		CalculatorRequestDTO inputDTO = new CalculatorRequestDTO("2.3*2.3+5");
		List<Token> anyTokens = List.of();
		List<Token> anyTokensInPostFix = List.of();
				
		BigDecimal expectedResult = BigDecimal.valueOf(10.29);
		
		BDDMockito.when(resultModelRepository.findByExpression(ArgumentMatchers.anyString()))
			.thenReturn(Optional.empty());
		BDDMockito.when(breakExpression.execute(ArgumentMatchers.anyString()))
			.thenReturn(anyTokens);
		BDDMockito.when(shuntingYardAlgorithm.transformToPostFixNotation(ArgumentMatchers.anyList()))
			.thenReturn(anyTokensInPostFix);
		BDDMockito.when(reversePolishNotationCalculator.calculatePostfixNotation(ArgumentMatchers.anyList()))
			.thenReturn(BigDecimal.valueOf(10.29));

		CalculatorResposeDTO resultDto = assertDoesNotThrow(() -> calculatorServiceImpl.calculate(inputDTO));
		assertThat(resultDto.getResultado()).isEqualTo(expectedResult);

		BDDMockito.verify(resultModelRepository, times(1)).findByExpression(inputDTO.getExpressao());
		BDDMockito.verify(breakExpression, times(1)).execute(inputDTO.getExpressao());
		BDDMockito.verify(shuntingYardAlgorithm, times(1)).transformToPostFixNotation(anyTokens);
		BDDMockito.verify(reversePolishNotationCalculator, times(1)).calculatePostfixNotation(anyTokensInPostFix);
		BDDMockito.verify(resultModelRepository, times(1)).save(ArgumentMatchers.any(ResultModel.class));		
	}

	@Test
	@DisplayName("returns 0.78 If expression is equals to '2.33/3'")
	void returns0dot78_ifExpressionIsEqualTo2dot33DividedBy3() {
		CalculatorRequestDTO inputDTO = new CalculatorRequestDTO("2.33/3");
		List<Token> tokens = List.of(
				new Token("2.33", TokenType.NUMBER, 0),
				new Token("/", TokenType.BINARY_OPERATORS, 1),
				new Token("3", TokenType.NUMBER, 0));

		List<Token> tokensInPostFix = List.of(
				new Token("2.33", TokenType.NUMBER, 0),
				new Token("3", TokenType.NUMBER, 0),
				new Token("/", TokenType.BINARY_OPERATORS, 1));
				
		BigDecimal expectedResult = BigDecimal.valueOf(0.78);
		
		BDDMockito.when(resultModelRepository.findByExpression(ArgumentMatchers.anyString()))
			.thenReturn(Optional.empty());
		BDDMockito.when(breakExpression.execute(ArgumentMatchers.anyString()))
			.thenReturn(tokens);
		BDDMockito.when(shuntingYardAlgorithm.transformToPostFixNotation(ArgumentMatchers.anyList()))
			.thenReturn(tokensInPostFix);
		BDDMockito.when(reversePolishNotationCalculator.calculatePostfixNotation(ArgumentMatchers.anyList()))
			.thenReturn(BigDecimal.valueOf(0.776666667));

		CalculatorResposeDTO resultDto = assertDoesNotThrow(() -> calculatorServiceImpl.calculate(inputDTO));
		assertThat(resultDto.getResultado()).isEqualTo(expectedResult);

		BDDMockito.verify(resultModelRepository, times(1)).findByExpression(inputDTO.getExpressao());
		BDDMockito.verify(breakExpression, times(1)).execute(inputDTO.getExpressao());
		BDDMockito.verify(shuntingYardAlgorithm, times(1)).transformToPostFixNotation(tokens);
		BDDMockito.verify(reversePolishNotationCalculator, times(1)).calculatePostfixNotation(tokensInPostFix);
		BDDMockito.verify(resultModelRepository, times(1)).save(ArgumentMatchers.any(ResultModel.class));		
	}

	@Test
	@DisplayName("throws If expression is equals to '1/0'")
	void throws_ifExpressionIsEqualToOneividedByZero() {
		CalculatorRequestDTO inputDTO = new CalculatorRequestDTO("1/0");
		List<Token> tokens = List.of(
				new Token("1", TokenType.NUMBER, 0),
				new Token("/", TokenType.BINARY_OPERATORS, 1),
				new Token("0", TokenType.NUMBER, 0));

		List<Token> tokensInPostFix = List.of(
				new Token("1", TokenType.NUMBER, 0),
				new Token("0", TokenType.NUMBER, 0),
				new Token("/", TokenType.BINARY_OPERATORS, 1));
				
		
		BDDMockito.when(resultModelRepository.findByExpression(ArgumentMatchers.anyString()))
			.thenReturn(Optional.empty());
		BDDMockito.when(breakExpression.execute(ArgumentMatchers.anyString()))
			.thenReturn(tokens);
		BDDMockito.when(shuntingYardAlgorithm.transformToPostFixNotation(ArgumentMatchers.anyList()))
			.thenReturn(tokensInPostFix);
		BDDMockito.when(reversePolishNotationCalculator.calculatePostfixNotation(ArgumentMatchers.anyList()))
			.thenThrow(new RuntimeException("any message"));

		Assertions.assertThatThrownBy(() -> calculatorServiceImpl.calculate(inputDTO))
			.isInstanceOf(RuntimeException.class)
			.hasMessage("any message");
		
		BDDMockito.verify(resultModelRepository, times(1)).findByExpression(inputDTO.getExpressao());
		BDDMockito.verify(breakExpression, times(1)).execute(inputDTO.getExpressao());
		BDDMockito.verify(shuntingYardAlgorithm, times(1)).transformToPostFixNotation(tokens);
		BDDMockito.verify(reversePolishNotationCalculator, times(1)).calculatePostfixNotation(tokensInPostFix);
		BDDMockito.verify(resultModelRepository, times(0)).save(ArgumentMatchers.any(ResultModel.class));		
	}
}
