package com.secretariaculturacarabobo.cultistregistration.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.secretariaculturacarabobo.cultistregistration.backend.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    public User findByUsername(String username);

}
