package com.luizvaldiero.calculator.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.luizvaldiero.calculator.enums.TokenType;

@ExtendWith(SpringExtension.class)
class ExpressionTreeBuilderTest {

	static ExpressionTreeBuilder expressionTreeBuilder;
	
	private static void assertLeafExpressionNode(ExpressionNode node, Token token) {
		assertThat(node).isInstanceOfAny(LeafExpressionNode.class);
		assertThat(node).hasFieldOrPropertyWithValue("token", token);
		assertThat(node.getLeft()).isNull();
		assertThat(node.getRight()).isNull();
	}
	
	@BeforeAll
	public static void setup() {
		expressionTreeBuilder = new ExpressionTreeBuilder();
	}
	
	@Test
	@DisplayName("return tree with addition")
	void treeWithAddition() {
		List<Token> tokens = List.of(
				new Token("left", TokenType.NUMBER),
				new Token("root", TokenType.ADDITION),
				new Token("right", TokenType.NUMBER)
				);
		ExpressionNode root = expressionTreeBuilder.create(tokens);
		ExpressionNode left = root.getLeft();
		ExpressionNode right = root.getRight();
		
		assertThat(root).isInstanceOfAny(BranchExpressionNode.class);
		assertThat(root).hasFieldOrProperty("calculator");
		assertLeafExpressionNode(left, tokens.get(0));
		assertLeafExpressionNode(right, tokens.get(2));
	}
	
	@Test
	@DisplayName("return tree with subtraction")
	void treeWithSubtraction() {
		List<Token> tokens = List.of(
				new Token("left", TokenType.NUMBER),
				new Token("root", TokenType.SUBTRACTION),
				new Token("right", TokenType.NUMBER)
				);
		ExpressionNode root = expressionTreeBuilder.create(tokens);
		ExpressionNode left = root.getLeft();
		ExpressionNode right = root.getRight();
		
		assertThat(root).isInstanceOfAny(BranchExpressionNode.class);
		assertThat(root).hasFieldOrProperty("calculator");
		assertLeafExpressionNode(left, tokens.get(0));
		assertLeafExpressionNode(right, tokens.get(2));
	}
	
	@Test
	@DisplayName("return tree with multiplication")
	void treeWithMultiplication() {
		List<Token> tokens = List.of(
				new Token("left", TokenType.NUMBER),
				new Token("root", TokenType.MULTIPLICATION),
				new Token("right", TokenType.NUMBER)
				);
		ExpressionNode root = expressionTreeBuilder.create(tokens);
		ExpressionNode left = root.getLeft();
		ExpressionNode right = root.getRight();
		
		assertThat(root).isInstanceOfAny(BranchExpressionNode.class);
		assertThat(root).hasFieldOrProperty("calculator");
		assertLeafExpressionNode(left, tokens.get(0));
		assertLeafExpressionNode(right, tokens.get(2));
	}
	
	@Test
	@DisplayName("return tree with division")
	void treeWithDivision() {
		List<Token> tokens = List.of(
				new Token("left", TokenType.NUMBER),
				new Token("root", TokenType.DIVISION),
				new Token("right", TokenType.NUMBER)
				);
		ExpressionNode root = expressionTreeBuilder.create(tokens);
		ExpressionNode left = root.getLeft();
		ExpressionNode right = root.getRight();
		
		assertThat(root).isInstanceOfAny(BranchExpressionNode.class);
		assertThat(root).hasFieldOrProperty("calculator");
		assertLeafExpressionNode(left, tokens.get(0));
		assertLeafExpressionNode(right, tokens.get(2));
	}
	
	@Test
	@DisplayName("return tree with division and subtraction")
	void treeWithDivisionAndSubtraction() {
		List<Token> tokens = List.of(
				new Token("3-1", TokenType.NUMBER),
				new Token("2-1", TokenType.DIVISION),
				new Token("3-2", TokenType.NUMBER),
				new Token("1", TokenType.SUBTRACTION),
				new Token("2-2", TokenType.NUMBER)
				);
		ExpressionNode root = expressionTreeBuilder.create(tokens);
		ExpressionNode n21 = root.getLeft();
		ExpressionNode n22 = root.getRight();
		ExpressionNode n31 = n21.getLeft();
		ExpressionNode n32 = n21.getRight();
		
		assertThat(root).isInstanceOfAny(BranchExpressionNode.class);
		assertThat(root).hasFieldOrProperty("calculator");
		assertThat(n21).isInstanceOfAny(BranchExpressionNode.class);
		assertThat(n21).hasFieldOrProperty("calculator");

		assertLeafExpressionNode(n22, tokens.get(4));
		assertLeafExpressionNode(n31, tokens.get(0));
		assertLeafExpressionNode(n32, tokens.get(2));
	}
	
	@Test
	@DisplayName("return tree with subtraction and division")
	void treeWithSubtractionAndDivision() {
		List<Token> tokens = List.of(
				new Token("2-1", TokenType.NUMBER),
				new Token("1", TokenType.SUBTRACTION),
				new Token("3-1", TokenType.NUMBER),
				new Token("2-2", TokenType.DIVISION),
				new Token("3-2", TokenType.NUMBER)
				);
		ExpressionNode root = expressionTreeBuilder.create(tokens);
		ExpressionNode n21 = root.getLeft();
		ExpressionNode n22 = root.getRight();
		ExpressionNode n31 = n22.getLeft();
		ExpressionNode n32 = n22.getRight();
		
		assertThat(root).isInstanceOfAny(BranchExpressionNode.class);
		assertThat(root).hasFieldOrProperty("calculator");
		
		assertLeafExpressionNode(n21, tokens.get(0));
		assertThat(n22).isInstanceOfAny(BranchExpressionNode.class);
		assertThat(n22).hasFieldOrProperty("calculator");

		assertLeafExpressionNode(n31, tokens.get(2));
		assertLeafExpressionNode(n32, tokens.get(4));
	}

}
