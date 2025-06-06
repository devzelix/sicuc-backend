package com.culturacarabobo.sicuc.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.culturacarabobo.sicuc.backend.entities.Parish;

/**
 * Repository interface for accessing Parish entities.
 */
public interface ParishRepository extends JpaRepository<Parish, Integer> {

}
