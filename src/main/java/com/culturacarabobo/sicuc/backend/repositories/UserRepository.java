package com.culturacarabobo.sicuc.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.culturacarabobo.sicuc.backend.entities.User;
import org.springframework.security.core.userdetails.UserDetails; // Import for Javadoc

/**
 * Spring Data JPA repository for the {@link User} entity.
 * <p>
 * This interface provides standard CRUD operations by extending
 * {@link JpaRepository}
 * and is used by the {@link com.culturacarabobo.sicuc.backend.services.UserService}
 * to fetch user details.
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    /**
     * Finds a user by their unique username.
     * <p>
     * This method is used by the {@code UserDetailsService} to load a
     * {@link UserDetails} object during authentication.
     *
     * @param username The username to search for.
     * @return The {@link User} entity if found, or {@code null} if not found.
     */
    public User findByUsername(String username);

}