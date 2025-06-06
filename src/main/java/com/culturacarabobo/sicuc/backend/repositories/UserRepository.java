package com.culturacarabobo.sicuc.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.culturacarabobo.sicuc.backend.entities.User;

public interface UserRepository extends JpaRepository<User, Integer> {

    public User findByUsername(String username);

}
