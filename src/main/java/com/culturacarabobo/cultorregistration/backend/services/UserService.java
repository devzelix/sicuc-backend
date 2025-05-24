package com.culturacarabobo.cultorregistration.backend.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.culturacarabobo.cultorregistration.backend.entities.User;
import com.culturacarabobo.cultorregistration.backend.repositories.UserRepository;

/**
 * Service class responsible for loading user-specific data for authentication.
 * Implements UserDetailsService from Spring Security to integrate with the
 * security framework.
 */
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {

        this.userRepository = userRepository;

    }

    /**
     * Loads a user by their username.
     * Used by Spring Security during the authentication process.
     * 
     * @param username the username identifying the user whose data is required.
     * @return UserDetails object containing user information.
     * @throws UsernameNotFoundException if the user is not found in the repository.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            // Throw exception if user not found to trigger authentication failure
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // Return the User entity which implements UserDetails
        return user;
    }

}
