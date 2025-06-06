package com.culturacarabobo.sicuc.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.culturacarabobo.sicuc.backend.entities.ArtDiscipline;

/**
 * Repository interface for accessing Art Discipline entities.
 */
public interface ArtDisciplineRepository extends JpaRepository<ArtDiscipline, Integer> {

}