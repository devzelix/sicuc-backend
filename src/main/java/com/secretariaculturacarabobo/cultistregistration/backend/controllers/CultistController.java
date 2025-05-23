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

/**
 * REST controller to manage Cultist entities.
 * Supports filtering and creation of cultists via HTTP endpoints.
 */
@RestController
@RequestMapping("/cultists")
public class CultistController {

    private final CultistService cultistService;

    /**
     * Constructor for dependency injection of the CultistService.
     * 
     * @param cultistService Service that handles business logic for cultists.
     */
    public CultistController(CultistService cultistService) {
        this.cultistService = cultistService;
    }

    /**
     * GET endpoint to retrieve a list of cultists filtered by optional parameters.
     * 
     * @param query           Optional search query to match cultist names, IDs,
     *                        etc.
     * @param gender          Optional filter by gender.
     * @param municipalityId  Optional filter by municipality ID.
     * @param parishId        Optional filter by parish ID.
     * @param artCategoryId   Optional filter by art category ID.
     * @param artDisciplineId Optional filter by art discipline ID.
     * @param hasDisability   Optional filter by presence of disability.
     * @param hasIllness      Optional filter by presence of illness.
     * @return List of CultistResponse DTOs matching the filters.
     */
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

    /**
     * POST endpoint to create a new cultist.
     * Validates the request body before passing it to the service layer.
     * 
     * @param cultistRequest DTO containing the data to create a cultist.
     * @return ResponseEntity with the created CultistResponse and appropriate HTTP
     *         status.
     */
    @PostMapping
    public ResponseEntity<CultistResponse> create(@Valid @RequestBody CultistRequest cultistRequest) {
        return cultistService.create(cultistRequest);
    }

}
