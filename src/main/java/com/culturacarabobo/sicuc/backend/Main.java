package com.culturacarabobo.sicuc.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Main entry point for the SICUC backend Spring Boot
 * application.
 * <p>
 * This class bootstraps and launches the Spring application context.
 * It also enables {@link EnableJpaAuditing} to support automatic timestamp
 * handling
 * (e.g., the {@code createdAt} field in the {@link entities.Cultor} entity).
 */
@SpringBootApplication
@EnableJpaAuditing
public class Main {

    /**
     * Main method to start the Spring Boot application.
     *
     * @param args Command-line arguments passed during application startup.
     */
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }

}