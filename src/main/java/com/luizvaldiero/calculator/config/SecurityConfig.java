package com.luizvaldiero.calculator.config;

import java.security.interfaces.RSAPublicKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Value("${security.jwt.key-value}")
	RSAPublicKey key;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.authorizeRequests((authorizeRequests) -> 
				authorizeRequests.anyRequest().authenticated()
			)
			.oauth2ResourceServer((oauth2ResourceServer) -> 
				oauth2ResourceServer
					.jwt((jwt) -> 
						jwt.decoder(jwtDecoder())
					)
			);
	}

	@Bean
	JwtDecoder jwtDecoder() {
		return NimbusJwtDecoder.withPublicKey(this.key).build();
	}
}
