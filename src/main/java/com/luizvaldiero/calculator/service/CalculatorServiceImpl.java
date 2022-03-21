package com.luizvaldiero.calculator.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.luizvaldiero.calculator.component.BreakExpression;
import com.luizvaldiero.calculator.component.ReversePolishNotationCalculator;
import com.luizvaldiero.calculator.component.ShuntingYardAlgorithm;
import com.luizvaldiero.calculator.dto.CalculatorRequestDTO;
import com.luizvaldiero.calculator.dto.CalculatorResposeDTO;
import com.luizvaldiero.calculator.model.ResultModel;
import com.luizvaldiero.calculator.model.Token;
import com.luizvaldiero.calculator.model.lexical.ExtractToken;
import com.luizvaldiero.calculator.model.lexical.ExtractTokenAddition;
import com.luizvaldiero.calculator.model.lexical.ExtractTokenDivision;
import com.luizvaldiero.calculator.model.lexical.ExtractTokenMultiplication;
import com.luizvaldiero.calculator.model.lexical.ExtractTokenNumber;
import com.luizvaldiero.calculator.model.lexical.ExtractTokenSubtraction;
import com.luizvaldiero.calculator.repository.ResultModelRepository;

@Service
public class CalculatorServiceImpl implements CalculatorService {
	private static final int N_PRECISION = 2;
	
	private final BreakExpression breakExpression;
	private final ResultModelRepository resultModelRepository;
	
	private final ShuntingYardAlgorithm shuntingYardAlgorithm;
	private final ReversePolishNotationCalculator reversePolishNotationCalculator;
	
	@Autowired
	public CalculatorServiceImpl(
			BreakExpression breakExpression,
			ShuntingYardAlgorithm shuntingYardAlgorithm,
			ReversePolishNotationCalculator reversePolishNotationCalculator,
			ResultModelRepository resultModelRepository
	) {
		this.breakExpression = breakExpression;
		this.shuntingYardAlgorithm = shuntingYardAlgorithm;
		this.reversePolishNotationCalculator = reversePolishNotationCalculator;
		this.resultModelRepository = resultModelRepository;
	}
	
	@PostConstruct
	public void initialize() {
		ExtractToken extractToken = new ExtractTokenAddition();
		extractToken
			.setNextExtracToken(new ExtractTokenAddition())
			.setNextExtracToken(new ExtractTokenSubtraction())
			.setNextExtracToken(new ExtractTokenMultiplication())
			.setNextExtracToken(new ExtractTokenDivision())
			.setNextExtracToken(new ExtractTokenNumber());
		breakExpression.setExtractToken(extractToken);
	}

	@Override
	public CalculatorResposeDTO calculate(CalculatorRequestDTO request) {
		String expression = removeWhitespace(request.getExpressao());
		
		Optional<ResultModel> resultModalOpt = resultModelRepository.findByExpression(expression);
		
		if (resultModalOpt.isPresent()) {
			BigDecimal result = resultModalOpt.get().getResult();
			return new CalculatorResposeDTO(result);
		}
		
		List<Token> tokensFromInFixNotation = breakExpression.execute(expression);

		List<Token> tokensInPostFixNotation = shuntingYardAlgorithm.transformToPostFixNotation(tokensFromInFixNotation);
		
		BigDecimal result = reversePolishNotationCalculator.calculateInfixNotation(tokensInPostFixNotation);
		
		if (result.scale() > N_PRECISION) {
			result = result.setScale(N_PRECISION, RoundingMode.UP);
		}
		
		ResultModel resultModel = new ResultModel(expression, result);
		
		try {
			resultModelRepository.save(resultModel);
		} catch (DataIntegrityViolationException e) {
			e.printStackTrace();
		}
		
		return new CalculatorResposeDTO(result);
	}
	
	private String removeWhitespace(String expression) {
		return expression.replaceAll("\s", "");
	}
}
