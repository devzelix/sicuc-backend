package com.culturacarabobo.sicuc.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.culturacarabobo.sicuc.backend.entities.Cultor;

/**
 * Repository interface for accessing Cultor entities.
 */
public interface CultorRepository extends JpaRepository<Cultor, Integer>, JpaSpecificationExecutor<Cultor> {

    /**
     * Checks if a cultor exists by their ID number.
     */
    boolean existsByIdNumber(String idNumber);

    /**
     * Checks if a cultor exists by their phone number.
     */
    boolean existsByPhoneNumber(String phoneNumber);

    /**
     * Checks if a cultor exists by their email address.
     */
    boolean existsByEmail(String email);

    /**
     * Checks if a cultor exists by their Instagram username.
     */
    boolean existsByInstagramUser(String instragramUser);

}
