package com.secretariaculturacarabobo.cultistregistration.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.secretariaculturacarabobo.cultistregistration.backend.entities.Cultist;

public interface CultistRepository extends JpaRepository<Cultist, Integer>, JpaSpecificationExecutor<Cultist> {

    boolean existsByIdNumber(String idNumber);

    boolean existsByPhoneNumber(String phoneNumber);

    boolean existsByEmail(String email);

    boolean existsByInstagramUser(String instragramUser);

}
