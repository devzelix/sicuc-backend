package com.culturacarabobo.cultorregistration.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.culturacarabobo.cultorregistration.backend.entities.ArtDiscipline;

/**
 * Repository interface for accessing Art Discipline entities.
 */
public interface ArtDisciplineRepository extends JpaRepository<ArtDiscipline, Integer> {

}