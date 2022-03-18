package com.luizvaldiero.calculator.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luizvaldiero.calculator.component.BreakExpression;
import com.luizvaldiero.calculator.component.ExpressionTreeBuilder;
import com.luizvaldiero.calculator.dto.CalculatorRequestDTO;
import com.luizvaldiero.calculator.dto.CalculatorResposeDTO;
import com.luizvaldiero.calculator.model.ExpressionNode;
import com.luizvaldiero.calculator.model.Token;

@Service
public class CalculatorServiceImpl implements CalculatorService {
	private static final int N_PRECISION = 2;
	
	private static final String NUMBER = "\\d+\\.?\\d*";
	private static final String OPERATORS = "((-\\+?)|(\\+-?)|([(\\*)/][\\+-]?))";
	private static final String VALID_EXPRESSION = "^([\\+-]?" + NUMBER + ")(" + OPERATORS + NUMBER + ")*$";
	
	private final BreakExpression breakExpression;
	private final ExpressionTreeBuilder expressionTreeBuilder;
	
	@Autowired
	public CalculatorServiceImpl(BreakExpression breakExpression, ExpressionTreeBuilder expressionTreeBuilder) {
		this.breakExpression = breakExpression;
		this.expressionTreeBuilder = expressionTreeBuilder;
	}
	
	@Override
	public CalculatorResposeDTO calculate(@Valid CalculatorRequestDTO request) {
		String expression = request.getExpressao().replaceAll("\s", "");
		Boolean isValid = expression.matches(VALID_EXPRESSION);
		if (!isValid) {
			throw new RuntimeException("Expressão mal formada");
		}
		
		List<Token> tokens = breakExpression.execute(expression);		
		ExpressionNode root = expressionTreeBuilder.create(tokens);
		
		BigDecimal result = root.calculate()
				.setScale(N_PRECISION, RoundingMode.UP);
		
		return new CalculatorResposeDTO(result);
	}

}
