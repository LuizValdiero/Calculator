package com.luizvaldiero.calculator.dto;

import javax.validation.constraints.NotBlank;

public class CalculatorRequestDTO {

	@NotBlank
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
