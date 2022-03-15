package com.luizvaldiero.calculator.dto;

public class CalculatorRequestDTO {

	private String expressao;

	public CalculatorRequestDTO() {}
	
	public CalculatorRequestDTO(String expressao) {
		this.expressao = expressao;
	}

	public String getExpressao() {
		return expressao;
	}

	public void setExpressao(String expressao) {
		this.expressao = expressao;
	}
}
