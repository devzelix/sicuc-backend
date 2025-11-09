package com.culturacarabobo.sicuc.backend.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.culturacarabobo.sicuc.backend.entities.Role;
import com.culturacarabobo.sicuc.backend.entities.User;
import com.culturacarabobo.sicuc.backend.repositories.UserRepository;

/**
 * Seeds the database with initial development data on application startup.
 * <p>
 * This component implements {@link CommandLineRunner}, causing its {@link #run}
 * method to be executed once the Spring application context is loaded.
 * <p>
 * It is annotated with {@link Profile("dev")} so it **only** runs when the
 * 'dev' Spring profile is active, preventing test data from being added to
 * a production environment.
 */
@Component
@Profile("dev")
public class DevDataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructs the seeder with required dependencies.
     *
     * @param userRepository  Repository for {@link User} data access.
     * @param passwordEncoder The bean used for hashing passwords.
     */
    public DevDataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * The main entry point executed by {@link CommandLineRunner}.
     * Delegates to the specific seeding methods.
     *
     * @param args Application arguments (not used).
     * @throws Exception if an error occurs during seeding.
     */
    @Override
    public void run(String... args) throws Exception {
        seedUsers();
    }

    /**
     * Seeds default test users (e.g., an 'admin' user) into the database
     * **only if** the user table is currently empty.
     */
    @SuppressWarnings("null")
    private void seedUsers() {
        // Only seed data if no users exist
        if (userRepository.count() == 0) {
            List<User> users = List.of(
                    new User("admin", passwordEncoder.encode("admin"), Role.ROLE_ADMIN)
            );
            userRepository.saveAll(users);
        }
    }

}