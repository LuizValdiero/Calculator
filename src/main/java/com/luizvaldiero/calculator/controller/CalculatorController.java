package com.luizvaldiero.calculator.controller;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.luizvaldiero.calculator.dto.CalculatorRequestDTO;
import com.luizvaldiero.calculator.dto.CalculatorResposeDTO;
import com.luizvaldiero.calculator.service.CalculatorService;

@Controller
@RequestMapping("/v1/calculator")
public class CalculatorController {
	
	private final CalculatorService calculatorService;

	public CalculatorController(CalculatorService calculatorService) {
		this.calculatorService = calculatorService;
	}
	
	@PostMapping
	public ResponseEntity<CalculatorResposeDTO> calculate(@RequestBody @Valid CalculatorRequestDTO request) {
		CalculatorResposeDTO result = calculatorService.calculate(request);
		return ResponseEntity.ok(result);
	}
	
}
