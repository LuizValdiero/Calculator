package com.luizvaldiero.calculator.service;

import com.luizvaldiero.calculator.dto.CalculatorRequestDTO;
import com.luizvaldiero.calculator.dto.CalculatorResposeDTO;

public interface CalculatorService {
	
	public CalculatorResposeDTO calculate(CalculatorRequestDTO request);
	
}
