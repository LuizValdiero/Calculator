package com.luizvaldiero.calculator;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(classes = CalculatorApplication.class)
@TestPropertySource(properties = {
		"spring.flyway.enabled=false",
	    "spring.jpa.generate-ddl=true",
	    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class CalculatorApplicationTests {

	@Test
	void contextLoads() {
	}

}
