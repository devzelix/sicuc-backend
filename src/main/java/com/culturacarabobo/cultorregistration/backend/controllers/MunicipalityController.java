package com.culturacarabobo.cultorregistration.backend.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.culturacarabobo.cultorregistration.backend.dtos.MunicipalityResponse;
import com.culturacarabobo.cultorregistration.backend.services.MunicipalityService;

import org.springframework.web.bind.annotation.GetMapping;

/**
 * REST controller to manage municipality-related endpoints.
 * Provides access to municipality data through HTTP requests.
 */
@RestController
@RequestMapping("/municipalities")
public class MunicipalityController {

    private final MunicipalityService municipalityService;

    /**
     * Constructor-based dependency injection for MunicipalityService.
     *
     * @param municipalityService service that handles municipality data operations
     */
    public MunicipalityController(MunicipalityService municipalityService) {
        this.municipalityService = municipalityService;
    }

    /**
     * Handles HTTP GET requests for retrieving all municipalities.
     *
     * @return a list of MunicipalityResponse DTOs representing all municipalities
     */
    @GetMapping
    public List<MunicipalityResponse> getAll() {
        return municipalityService.getAll();
    }

}
