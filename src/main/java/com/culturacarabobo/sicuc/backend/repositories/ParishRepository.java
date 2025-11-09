package com.culturacarabobo.sicuc.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.culturacarabobo.sicuc.backend.entities.Parish;

/**
 * Spring Data JPA repository for the {@link Parish} entity.
 * <p>
 * This interface automatically provides standard CRUD (Create, Read, Update,
 * Delete) operations for {@link Parish} entities by extending
 * {@link JpaRepository}.
 * <p>
 * It is used by the
 * {@link com.culturacarabobo.sicuc.backend.services.ParishService} and
 * {@link com.culturacarabobo.sicuc.backend.services.CultorService}.
 */
public interface ParishRepository extends JpaRepository<Parish, Integer> {

    // No custom query methods are needed for this entity.
    // Standard methods like findAll(), findById(), save(), deleteById() are
    // inherited from JpaRepository.
}