package com.secretariaculturacarabobo.cultistregistration.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main entry point for the Cultist Registration backend Spring Boot
 * application.
 * This class bootstraps and launches the Spring application context.
 * It also enables JPA auditing to support automatic timestamp handling.
 */
@SpringBootApplication
@EnableJpaAuditing
public class BackendApplication {

	/**
	 * Main method to start the Spring Boot application.
	 * 
	 * @param args Command-line arguments passed during application startup.
	 */
	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

}
