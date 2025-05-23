package com.secretariaculturacarabobo.cultistregistration.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.secretariaculturacarabobo.cultistregistration.backend.entities.Cultist;

/**
 * Repository interface for accessing Cultist entities.
 */
public interface CultistRepository extends JpaRepository<Cultist, Integer>, JpaSpecificationExecutor<Cultist> {

    /**
     * Checks if a cultist exists by their ID number.
     */
    boolean existsByIdNumber(String idNumber);

    /**
     * Checks if a cultist exists by their phone number.
     */
    boolean existsByPhoneNumber(String phoneNumber);

    /**
     * Checks if a cultist exists by their email address.
     */
    boolean existsByEmail(String email);

    /**
     * Checks if a cultist exists by their Instagram username.
     */
    boolean existsByInstagramUser(String instragramUser);

}
