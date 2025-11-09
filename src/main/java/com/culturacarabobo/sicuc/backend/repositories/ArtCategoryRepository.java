package com.culturacarabobo.sicuc.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.culturacarabobo.sicuc.backend.entities.ArtCategory;

/**
 * Spring Data JPA repository for the {@link ArtCategory} entity.
 * <p>
 * This interface automatically provides standard CRUD (Create, Read, Update,
 * Delete) operations for {@link ArtCategory} entities by extending
 * {@link JpaRepository}.
 * <p>
 * It is used by the
 * {@link com.culturacarabobo.sicuc.backend.services.ArtCategoryService}.
 */
public interface ArtCategoryRepository extends JpaRepository<ArtCategory, Integer> {

    // No custom query methods are needed for this entity.
    // Standard methods like findAll(), findById(), save(), deleteById() are
    // inherited from JpaRepository.
}