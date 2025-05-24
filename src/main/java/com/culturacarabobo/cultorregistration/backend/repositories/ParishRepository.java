package com.culturacarabobo.cultorregistration.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.culturacarabobo.cultorregistration.backend.entities.Parish;

/**
 * Repository interface for accessing Parish entities.
 */
public interface ParishRepository extends JpaRepository<Parish, Integer> {

}
