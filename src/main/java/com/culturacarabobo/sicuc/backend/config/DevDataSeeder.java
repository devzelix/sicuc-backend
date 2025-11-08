package com.culturacarabobo.sicuc.backend.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.culturacarabobo.sicuc.backend.entities.Role;
import com.culturacarabobo.sicuc.backend.entities.User;
import com.culturacarabobo.sicuc.backend.repositories.UserRepository;

@Component
@Profile("dev")
public class DevDataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DevDataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        seedUsers();
    }

    /**
     * Seeds default test users if the repository is empty.
     */
    @SuppressWarnings("null")
    private void seedUsers() {
        if (userRepository.count() == 0) {
            List<User> users = List.of(
                    new User("admin", passwordEncoder.encode("admin"), Role.ROLE_ADMIN)
            );
            userRepository.saveAll(users);
        }
    }

}