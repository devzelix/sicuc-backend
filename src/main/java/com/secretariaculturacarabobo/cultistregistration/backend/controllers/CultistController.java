package com.secretariaculturacarabobo.cultistregistration.backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.secretariaculturacarabobo.cultistregistration.backend.dtos.CultistRequest;
import com.secretariaculturacarabobo.cultistregistration.backend.dtos.CultistResponse;
import com.secretariaculturacarabobo.cultistregistration.backend.services.CultistService;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/cultists")
public class CultistController {

    private final CultistService cultistService;

    public CultistController(CultistService cultistService) {
        this.cultistService = cultistService;
    }

    @PostMapping
    public ResponseEntity<CultistResponse> create(@Valid @RequestBody CultistRequest cultistRequest) {
        return cultistService.create(cultistRequest);
    }

}
