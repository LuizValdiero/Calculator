package com.luizvaldiero.calculator.integration;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import com.luizvaldiero.calculator.dto.CalculatorRequestDTO;
import com.luizvaldiero.calculator.dto.CalculatorResposeDTO;
import com.luizvaldiero.calculator.exception.ExceptionDetails;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
		"spring.flyway.enabled=false",
	    "spring.jpa.generate-ddl=true",
	    "spring.jpa.hibernate.ddl-auto=create-drop",
	    "security.ignored: /**"
})
class CalculatorIntegrationTest {
	
	@Autowired
	private TestRestTemplate testRestTemplate;
	
	@LocalServerPort
	private int port;
	
	@Test
	@DisplayName("returns Unauthorized If not have a valid authentication token")
	void returnsUnauthorized_ifItDoesNotHaveAValidAuthenticationToken() {
		
		CalculatorRequestDTO requestBody = new CalculatorRequestDTO("1");
		
		ResponseEntity<Void> response = testRestTemplate.postForEntity("/v1/calculator", requestBody, null);
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
		assertThat(response.getBody()).isNull();
		
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer any.invalid.token");
		HttpEntity<CalculatorRequestDTO> requestInvalidToken = new HttpEntity<>(requestBody, headers);
		
		ResponseEntity<Void> responseInvalidToken = testRestTemplate.postForEntity("/v1/calculator", requestInvalidToken, null);
		
		assertThat(responseInvalidToken.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
		assertThat(responseInvalidToken.getBody()).isNull();
	}

	@Test
	@DisplayName("returns 4 If expression is equals to '2+2'")
	void returns4_ifExpressionIsEqualTo2Plus2() {
		
		CalculatorRequestDTO requestBody = new CalculatorRequestDTO("2+2");
		BigDecimal expected = BigDecimal.valueOf(4);
		
		HttpEntity<CalculatorRequestDTO> request = new HttpEntity<>(requestBody, this.createHeaders());
		ResponseEntity<CalculatorResposeDTO> response = testRestTemplate.postForEntity("/v1/calculator", request, CalculatorResposeDTO.class);
		CalculatorResposeDTO responseBody = response.getBody();
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseBody.getResultado()).isEqualByComparingTo(expected);
	}

	@Test
	@DisplayName("returns 4.4 If expression is equals to '2.2+2.2'")
	void returns4dot4_ifExpressionIsEqualTo2dot2Plus2dot2() {
		
		CalculatorRequestDTO requestBody = new CalculatorRequestDTO("2.2+2.2");
		BigDecimal expected = BigDecimal.valueOf(4.4);
		
		HttpEntity<CalculatorRequestDTO> request = new HttpEntity<>(requestBody, this.createHeaders());				
		ResponseEntity<CalculatorResposeDTO> response = testRestTemplate.postForEntity("/v1/calculator", request, CalculatorResposeDTO.class);
		CalculatorResposeDTO responseBody = response.getBody();
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseBody.getResultado()).isEqualByComparingTo(expected);
		
		ResponseEntity<CalculatorResposeDTO> responseSecond = testRestTemplate.postForEntity("/v1/calculator", request, CalculatorResposeDTO.class);
		CalculatorResposeDTO responseBodySecond = response.getBody();
		
		assertThat(responseSecond.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseBodySecond.getResultado()).isEqualByComparingTo(expected);
	}

	@Test
	@DisplayName("returns 10.29 If expression is equals to '2.3*2.3+5'")
	void returns10dot29_ifExpressionIsEqualTo2dot3Multiply2dot3Plus5() {
		
		CalculatorRequestDTO requestBody = new CalculatorRequestDTO("2.3*2.3+5");
		BigDecimal expected = BigDecimal.valueOf(10.29);
		
		HttpEntity<CalculatorRequestDTO> request = new HttpEntity<>(requestBody, this.createHeaders());
		ResponseEntity<CalculatorResposeDTO> response = testRestTemplate.postForEntity("/v1/calculator", request, CalculatorResposeDTO.class);
		CalculatorResposeDTO responseBody = response.getBody();
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseBody.getResultado()).isEqualByComparingTo(expected);
	}

	@Test
	@DisplayName("returns 0.78 If expression is equals to '2.33/3'")
	void returns0dot78_ifExpressionIsEqualTo2dot33DividedBy3() {
		
		CalculatorRequestDTO requestBody = new CalculatorRequestDTO("2.33/3");
		BigDecimal expected = BigDecimal.valueOf(0.78);
		
		HttpEntity<CalculatorRequestDTO> request = new HttpEntity<>(requestBody, this.createHeaders());
		ResponseEntity<CalculatorResposeDTO> response = testRestTemplate.postForEntity("/v1/calculator", request, CalculatorResposeDTO.class);
		CalculatorResposeDTO responseBody = response.getBody();
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(responseBody.getResultado()).isEqualByComparingTo(expected);
	}

	@Test
	@DisplayName("returns BadRequest If expression is equals to '1/0'")
	void returnsBadRequest_ifExpressionIsEqualToOneividedByZero(){
		
		CalculatorRequestDTO requestBody = new CalculatorRequestDTO("1/0");
		String expectedMessageError = "/ by zero";
		Integer expectedStatusCode = 400;
		
		HttpEntity<CalculatorRequestDTO> request = new HttpEntity<>(requestBody, this.createHeaders());
		ResponseEntity<ExceptionDetails> response = testRestTemplate.postForEntity("/v1/calculator", request, ExceptionDetails.class);
		ExceptionDetails exceptionDetails = response.getBody();
		
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
		assertThat(exceptionDetails.getError()).isEqualTo(expectedMessageError);
		assertThat(exceptionDetails.getStatusCode()).isEqualTo(expectedStatusCode);
	}

	private HttpHeaders createHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9."
				+ "eyJzdWIiOiJDYWxjdWxhdG9yQXV0aGVudGljYXRpb25Ub2tlbiIsImlhdCI6MTU"
				+ "xNjIzOTAyMn0.VABuaCLS7IJJUa9VsswYjnx1SGVuJJgOFyc8GtFLD8dpkgqqNh"
				+ "dhlR57d68XoeTMoU10ytj9bI2gEO3k-9HlOKWgP37FHpYWG3gDWqO9WBDAXwSJR"
				+ "y7c4jdvXloXTD4kuq3t-AQ6BoASN4iyYPBcr7lkhw4pPVc840vaQ9hnvmqsqf4x"
				+ "ogqwKl6wAdj33weaq25Gpfxo7QSaq2yl9lcsIkLZHvIjv2YEDLi2EQVNGbPJzp8"
				+ "gUrCjUmpfw7I6jFHQ0RQkTwQioO7wVtP76zsPjCrjbDNnOYDhMKo1DfNvtcGp8o"
				+ "lW8QsLtfGSd6ZR8ZGg4BFzPqdaUHcwJFEFK_ZaEw");
		return headers;
	}
}
