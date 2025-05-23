package com.secretariaculturacarabobo.cultistregistration.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.secretariaculturacarabobo.cultistregistration.backend.entities.Parish;

/**
 * Repository interface for accessing Parish entities.
 */
public interface ParishRepository extends JpaRepository<Parish, Integer> {

}
