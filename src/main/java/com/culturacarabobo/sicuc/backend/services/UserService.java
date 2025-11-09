package com.culturacarabobo.sicuc.backend.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.culturacarabobo.sicuc.backend.entities.User;
import com.culturacarabobo.sicuc.backend.repositories.UserRepository;

/**
 * Service class responsible for loading user-specific data for Spring Security.
 * <p>
 * This class implements {@link UserDetailsService}, which provides the core
 * method {@link #loadUserByUsername(String)} used by the
 * {@link org.springframework.security.authentication.AuthenticationProvider}
 * to fetch user details during authentication.
 */
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Constructs the service with the required {@link UserRepository}.
     *
     * @param userRepository Repository for {@link User} data access.
     */
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Locates the user based on the username.
     * <p>
     * This method is called by Spring Security's {@code AuthenticationProvider}
     * when a user attempts to log in.
     *
     * @param username The username identifying the user whose data is required.
     * @return A {@link UserDetails} object (in this case, our {@link User}
     * entity) containing the user's information.
     * @throws UsernameNotFoundException If the user is not found, which signals an
     * authentication failure.
     */
    @SuppressWarnings("null")
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        
        if (user == null) {
            // This exception is caught by Spring Security to indicate a failed login
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        // The User entity implements UserDetails, so it can be returned directly
        return user;
    }

}