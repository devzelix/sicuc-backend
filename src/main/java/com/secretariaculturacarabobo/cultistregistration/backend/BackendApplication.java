package com.secretariaculturacarabobo.cultistregistration.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main entry point for the Cultist Registration backend Spring Boot
 * application.
 * This class bootstraps and launches the entire Spring context.
 */

@SpringBootApplication
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
