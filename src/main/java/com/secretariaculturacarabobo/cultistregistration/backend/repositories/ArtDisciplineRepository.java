package com.secretariaculturacarabobo.cultistregistration.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.secretariaculturacarabobo.cultistregistration.backend.entities.ArtDiscipline;

/**
 * Repository interface for accessing Art Discipline entities.
 */
public interface ArtDisciplineRepository extends JpaRepository<ArtDiscipline, Integer> {

}