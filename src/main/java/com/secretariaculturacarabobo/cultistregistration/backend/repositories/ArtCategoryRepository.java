package com.secretariaculturacarabobo.cultistregistration.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.secretariaculturacarabobo.cultistregistration.backend.entities.ArtCategory;

/**
 * Repository interface for accessing Art Category entities.
 */
public interface ArtCategoryRepository extends JpaRepository<ArtCategory, Integer> {

}
