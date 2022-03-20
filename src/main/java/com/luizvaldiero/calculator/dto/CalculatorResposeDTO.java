package com.luizvaldiero.calculator.dto;

import java.math.BigDecimal;

public class CalculatorResposeDTO {
	BigDecimal resultado;

	public CalculatorResposeDTO() { }

	public CalculatorResposeDTO(BigDecimal resultado) {
		this.resultado = resultado;
	}

	public BigDecimal getResultado() {
		return resultado;
	}

	public void setResultado(BigDecimal resultado) {
		this.resultado = resultado;
	}	
}
