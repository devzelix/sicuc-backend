package com.culturacarabobo.sicuc.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.culturacarabobo.sicuc.backend.entities.Cultor;

/**
 * Spring Data JPA repository for the {@link Cultor} entity.
 * <p>
 * This interface provides standard CRUD operations (Create, Read, Update, Delete)
 * by extending {@link JpaRepository}.
 * <p>
 * It also extends {@link JpaSpecificationExecutor} to allow for dynamic,
 * criteria-based queries (e.g., filtering) using {@link com.culturacarabobo.sicuc.backend.specifications.CultorSpecification}.
 */
public interface CultorRepository extends JpaRepository<Cultor, Integer>, JpaSpecificationExecutor<Cultor> {

    /**
     * Checks if a {@link Cultor} exists with the given ID number.
     *
     * @param idNumber The ID number to check.
     * @return {@code true} if a cultor with this ID number exists, {@code false}
     * otherwise.
     */
    boolean existsByIdNumber(String idNumber);

    /**
     * Checks if a {@link Cultor} exists with the given phone number.
     *
     * @param phoneNumber The phone number to check.
     * @return {@code true} if a cultor with this phone number exists, {@code false}
     * otherwise.
     */
    boolean existsByPhoneNumber(String phoneNumber);

    /**
     * Checks if a {@link Cultor} exists with the given email address.
     *
     * @param email The email address to check.
     * @return {@code true} if a cultor with this email exists, {@code false}
     * otherwise.
     */
    boolean existsByEmail(String email);

    /**
     * Checks if a {@link Cultor} exists with the given Instagram username.
     *
     * @param instagramUser The Instagram username to check.
     * @return {@code true} if a cultor with this username exists, {@code false}
     * otherwise.
     */
    boolean existsByInstagramUser(String instagramUser);

    /**
     * Checks if a {@link Cultor} exists with the given phone number,
     * *excluding* a specific cultor ID.
     * <p>
     * This is used in the 'update' logic to see if a new phone number conflicts
     * with *another* user.
     *
     * @param phoneNumber The phone number to check.
     * @param id          The ID of the cultor to exclude from the check.
     * @return {@code true} if a *different* cultor uses this phone number,
     * {@code false} otherwise.
     */
    boolean existsByPhoneNumberAndIdNot(String phoneNumber, Integer id);

    /**
     * Checks if a {@link Cultor} exists with the given email,
     * *excluding* a specific cultor ID.
     *
     * @param email The email to check.
     * @param id    The ID of the cultor to exclude from the check.
     * @return {@code true} if a *different* cultor uses this email, {@code false}
     * otherwise.
     */
    boolean existsByEmailAndIdNot(String email, Integer id);

    /**
     * Checks if a {@link Cultor} exists with the given Instagram username,
     * *excluding* a specific cultor ID.
     *
     * @param instagramUser The Instagram username to check.
     * @param id            The ID of the cultor to exclude from the check.
     * @return {@code true} if a *different* cultor uses this username,
     * {@code false} otherwise.
     */
    boolean existsByInstagramUserAndIdNot(String instagramUser, Integer id);

    // Note: existsByIdNumberAndIdNot is not included as IdNumber is immutable
    // and this check is handled in the service layer.
}