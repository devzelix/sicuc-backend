package com.secretariaculturacarabobo.cultistregistration.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.secretariaculturacarabobo.cultistregistration.backend.entities.Cultist;

public interface CultistRepository extends JpaRepository<Cultist, Integer> {

}
