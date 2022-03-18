package com.luizvaldiero.calculator.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.luizvaldiero.calculator.model.ResultModel;

@Repository
public interface ResultModelRepository extends JpaRepository<ResultModel, String>{

	Optional<ResultModel> findByExpression(String expression);
	
}
