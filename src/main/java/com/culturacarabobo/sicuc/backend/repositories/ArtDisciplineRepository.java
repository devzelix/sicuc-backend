package com.culturacarabobo.sicuc.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.culturacarabobo.sicuc.backend.entities.ArtDiscipline;

/**
 * Spring Data JPA repository for the {@link ArtDiscipline} entity.
 * <p>
 * This interface automatically provides standard CRUD (Create, Read, Update,
 * Delete) operations for {@link ArtDiscipline} entities by extending
 * {@link JpaRepository}.
 * <p>
 * It is used by the
 * {@link com.culturacarabobo.sicuc.backend.services.ArtDisciplineService} and
 * {@link com.culturacarabobo.sicuc.backend.services.CultorService}.
 */
public interface ArtDisciplineRepository extends JpaRepository<ArtDiscipline, Integer> {

    // No custom query methods are needed for this entity.
    // Standard methods like findAll(), findById(), save(), deleteById() are
    // inherited from JpaRepository.
}