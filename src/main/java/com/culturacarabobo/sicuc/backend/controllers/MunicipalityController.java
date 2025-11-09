package com.culturacarabobo.sicuc.backend.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.culturacarabobo.sicuc.backend.dtos.MunicipalityResponse;
import com.culturacarabobo.sicuc.backend.services.MunicipalityService;

import org.springframework.web.bind.annotation.GetMapping;

/**
 * REST controller for exposing read-only endpoints related to Municipalities.
 * <p>
 * This controller is primarily used to provide data for frontend form
 * dropdowns.
 * It delegates all logic to {@link MunicipalityService}.
 */
@RestController
@RequestMapping("/municipalities")
public class MunicipalityController {

    private final MunicipalityService municipalityService;

    /**
     * Constructs the controller with the required MunicipalityService.
     *
     * @param municipalityService The service responsible for municipality
     * business logic.
     */
    public MunicipalityController(MunicipalityService municipalityService) {
        this.municipalityService = municipalityService;
    }

    /**
     * [GET /municipalities] Retrieves a list of all municipalities.
     * <p>
     * This endpoint is public and used to populate selection fields.
     *
     * @return A {@link List} of {@link MunicipalityResponse} DTOs, sorted by ID.
     */
    @GetMapping
    public List<MunicipalityResponse> getAll() {
        return municipalityService.getAll();
    }

}