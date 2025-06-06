package com.culturacarabobo.sicuc.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.culturacarabobo.sicuc.backend.entities.Municipality;

/**
 * Repository interface for accessing Municipality entities.
 */
public interface MunicipalityRepository extends JpaRepository<Municipality, Integer> {

}
