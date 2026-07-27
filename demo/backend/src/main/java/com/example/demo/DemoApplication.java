package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
		info = @Info(
				title = "User API",
				version = "1.0",
				description = "API documentation for managing users"
		)
)
@SpringBootApplication
public class DemoApplication {

 	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

}
