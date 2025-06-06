package com.culturacarabobo.sicuc.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.culturacarabobo.sicuc.backend.entities.ArtCategory;

/**
 * Repository interface for accessing Art Category entities.
 */
public interface ArtCategoryRepository extends JpaRepository<ArtCategory, Integer> {

}
