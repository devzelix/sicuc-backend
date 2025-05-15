package com.secretariaculturacarabobo.cultistregistration.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.secretariaculturacarabobo.cultistregistration.backend.entities.Municipality;

public interface MunicipalityRepository extends JpaRepository<Municipality, Integer> {

}
