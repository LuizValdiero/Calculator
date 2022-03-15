package com.luizvaldiero.calculator.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.luizvaldiero.calculator.dto.CalculatorRequestDTO;

@ExtendWith(SpringExtension.class)
class CalculatorServiceImplTest {

	@InjectMocks
	CalculatorServiceImpl calculatorServiceImpl;
	
	@Test
	@DisplayName("Throws If the expression is malformed")
	void throws_malformed_expression() {

		List<String> expressions = List.of(
				"--10", "++10", "*10", "/10",
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
				"2+3", "2-3", "2*3", "2/3",
				"2+-3", "2-+3", "2*-3", "2*+3",
				"2/-3", "2/+3", "-2.3243-2.43",
				"-98*-2.44+32/5.00+76*3/1.5"
		);
		for(String expression: expressions) {
			CalculatorRequestDTO request = new CalculatorRequestDTO(expression);
			
			assertDoesNotThrow(() -> calculatorServiceImpl.calculate(request));			
		}
	}

}
