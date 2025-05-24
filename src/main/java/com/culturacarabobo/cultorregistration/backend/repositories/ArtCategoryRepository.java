package com.culturacarabobo.cultorregistration.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.culturacarabobo.cultorregistration.backend.entities.ArtCategory;

/**
 * Repository interface for accessing Art Category entities.
 */
public interface ArtCategoryRepository extends JpaRepository<ArtCategory, Integer> {

}
