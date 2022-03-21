package com.luizvaldiero.calculator.model;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class ResultModel {

	@Id
	private String expression;

	private BigDecimal result;
	
	public ResultModel() { }

	public ResultModel(String expression, BigDecimal result) {
		this.expression = expression;
		this.result = result;
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public BigDecimal getResult() {
		return result;
	}

	public void setResult(BigDecimal result) {
		this.result = result;
	}
}
