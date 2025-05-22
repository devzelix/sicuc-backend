package com.secretariaculturacarabobo.cultistregistration.backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.secretariaculturacarabobo.cultistregistration.backend.dtos.UserRequest;
import com.secretariaculturacarabobo.cultistregistration.backend.services.UserService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@Valid @RequestBody UserRequest userRequest) {
        return userService.authenticate(userRequest);
    }

}
