package com.culturacarabobo.cultorregistration.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.culturacarabobo.cultorregistration.backend.entities.Municipality;

/**
 * Repository interface for accessing Municipality entities.
 */
public interface MunicipalityRepository extends JpaRepository<Municipality, Integer> {

}
