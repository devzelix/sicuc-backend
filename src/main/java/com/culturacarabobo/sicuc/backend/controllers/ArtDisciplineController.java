package com.culturacarabobo.sicuc.backend.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.culturacarabobo.sicuc.backend.dtos.ArtDisciplineResponse;
import com.culturacarabobo.sicuc.backend.services.ArtDisciplineService;

import org.springframework.web.bind.annotation.GetMapping;

/**
 * REST controller for exposing read-only endpoints related to Art Disciplines.
 * <p>
 * This controller is primarily used to provide data for frontend form
 * dropdowns.
 * It delegates all logic to {@link ArtDisciplineService}.
 */
@RestController
@RequestMapping("/art-disciplines")
public class ArtDisciplineController {

    private final ArtDisciplineService artDisciplineService;

    /**
     * Constructs the controller with the required ArtDisciplineService.
     *
     * @param artDisciplineService The service responsible for art discipline
     * business logic.
     */
    public ArtDisciplineController(ArtDisciplineService artDisciplineService) {
        this.artDisciplineService = artDisciplineService;
    }

    /**
     * [GET /art-disciplines] Retrieves a list of all art disciplines.
     * <p>
     * This endpoint is public and used to populate selection fields.
     *
     * @return A {@link List} of {@link ArtDisciplineResponse} DTOs, sorted by ID.
     */
    @GetMapping
    public List<ArtDisciplineResponse> getAll() {
        return artDisciplineService.getAll();
    }

}