package com.luizvaldiero.calculator.service;

import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.validation.Valid;

import com.luizvaldiero.calculator.dto.CalculatorRequestDTO;
import com.luizvaldiero.calculator.dto.CalculatorResposeDTO;

public class CalculatorServiceImpl implements CalculatorService {
	private static final int N_PRECISION = 2;

	private static final String NUMBER = "\\d+\\.?\\d*";
	private static final String OPERATORS = "((-\\+?)|(\\+-?)|([(\\*)/][\\+-]?))";
	private static final String VALID_EXPRESSION = "^([\\+-]?" + NUMBER + ")(" + OPERATORS + NUMBER + ")*$";
	
	@Override
	public CalculatorResposeDTO calculate(@Valid CalculatorRequestDTO request) {
		String expression = request.getExpressao().replaceAll("\s", "");
		Boolean isValid = expression.matches(VALID_EXPRESSION);
		if (!isValid) {
			throw new RuntimeException("Expressão mal formada");
		}
/*
 // calculador

 1) receber expressao e verificar formação
 2) quebrar expressao nos operadores - parser -> gerar árvore
 3) fazer calculo recursivo com base na order de prioridade
*/


		BigDecimal result = new BigDecimal(2.237);
		result.setScale(N_PRECISION, RoundingMode.DOWN);
		return new CalculatorResposeDTO(result);
	}

}
