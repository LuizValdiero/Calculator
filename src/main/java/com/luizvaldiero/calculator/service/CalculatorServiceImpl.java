package com.luizvaldiero.calculator.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.luizvaldiero.calculator.component.BreakExpression;
import com.luizvaldiero.calculator.component.ExpressionTreeBuilder;
import com.luizvaldiero.calculator.component.ReversePolishNotationCalculator;
import com.luizvaldiero.calculator.component.ShuntingYardAlgorithm;
import com.luizvaldiero.calculator.dto.CalculatorRequestDTO;
import com.luizvaldiero.calculator.dto.CalculatorResposeDTO;
import com.luizvaldiero.calculator.model.ExpressionNode;
import com.luizvaldiero.calculator.model.ResultModel;
import com.luizvaldiero.calculator.model.Token;
import com.luizvaldiero.calculator.repository.ResultModelRepository;

@Service
public class CalculatorServiceImpl implements CalculatorService {
	private static final int N_PRECISION = 2;
	
	private static final String NUMBER = "\\d+\\.?\\d*";
	private static final String OPERATORS = "((-\\+?)|(\\+-?)|([(\\*)/][\\+-]?))";
	private static final String VALID_EXPRESSION = "^([\\+-]?" + NUMBER + ")(" + OPERATORS + NUMBER + ")*$";
	
	private final BreakExpression breakExpression;
	private final ExpressionTreeBuilder expressionTreeBuilder;
	private final ResultModelRepository resultModelRepository;
	
	@Autowired
	public CalculatorServiceImpl(
			BreakExpression breakExpression,
			ExpressionTreeBuilder expressionTreeBuilder,
			ResultModelRepository resultModelRepository
	) {
		this.breakExpression = breakExpression;
		this.expressionTreeBuilder = expressionTreeBuilder;
		this.resultModelRepository = resultModelRepository;
	}
	
	@Override
	public CalculatorResposeDTO calculate(CalculatorRequestDTO request) {
		String expression = request.getExpressao().replaceAll("\s", "");
		Boolean isValid = expression.matches(VALID_EXPRESSION);
		if (!isValid) {
			throw new RuntimeException("Expressão mal formada");
		}
		
		Optional<ResultModel> resultModalOpt = resultModelRepository.findByExpression(expression);
		
		if (resultModalOpt.isPresent()) {
			BigDecimal result = resultModalOpt.get().getResult();
			return new CalculatorResposeDTO(result);
		}
		
		List<Token> tokens = breakExpression.execute(expression);		
		//ExpressionNode root = expressionTreeBuilder.create(tokens);
		ShuntingYardAlgorithm sya = new ShuntingYardAlgorithm();
		List<Token> rpnList = sya.execute(tokens);
		ReversePolishNotationCalculator rpnCalculator = new ReversePolishNotationCalculator();
		
		BigDecimal result = rpnCalculator.calculateInfixNotation(rpnList)
				.setScale(N_PRECISION, RoundingMode.UP);
		
		try {
			ResultModel resultModel = new ResultModel(expression, result);
			resultModelRepository.save(resultModel);
		} catch (Exception e) {}
		
		return new CalculatorResposeDTO(new BigDecimal(result.toPlainString()));
	}

}
