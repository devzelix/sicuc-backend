package com.secretariaculturacarabobo.cultistregistration.backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.secretariaculturacarabobo.cultistregistration.backend.dtos.CultistRequest;
import com.secretariaculturacarabobo.cultistregistration.backend.dtos.CultistResponse;
import com.secretariaculturacarabobo.cultistregistration.backend.services.CultistService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/cultists")
public class CultistController {

    private final CultistService cultistService;

    public CultistController(CultistService cultistService) {
        this.cultistService = cultistService;
    }

    @GetMapping
    public List<CultistResponse> getCultoresFiltrados(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Integer municipalityId,
            @RequestParam(required = false) Integer parishId,
            @RequestParam(required = false) Integer artCategoryId,
            @RequestParam(required = false) Integer artDisciplineId,
            @RequestParam(required = false) Boolean hasDisability,
            @RequestParam(required = false) Boolean hasIllness) {
        return cultistService.getAllCultistsWithFilters(query,
                gender, municipalityId, parishId, artCategoryId, artDisciplineId, hasDisability, hasIllness);
    }

    @PostMapping
    public ResponseEntity<CultistResponse> create(@Valid @RequestBody CultistRequest cultistRequest) {
        return cultistService.create(cultistRequest);
    }

}
