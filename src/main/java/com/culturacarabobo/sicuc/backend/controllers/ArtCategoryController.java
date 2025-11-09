package com.culturacarabobo.sicuc.backend.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.culturacarabobo.sicuc.backend.dtos.ArtCategoryResponse;
import com.culturacarabobo.sicuc.backend.services.ArtCategoryService;

import org.springframework.web.bind.annotation.GetMapping;

/**
 * REST controller for exposing read-only endpoints related to Art Categories.
 * <p>
 * This controller is primarily used to provide data for frontend form
 * dropdowns.
 * It delegates all logic to {@link ArtCategoryService}.
 */
@RestController
@RequestMapping("/art-categories")
public class ArtCategoryController {

    private final ArtCategoryService artCategoryService;

    /**
     * Constructs the controller with the required ArtCategoryService.
     *
     * @param artCategoryService The service responsible for art category business
     * logic.
     */
    public ArtCategoryController(ArtCategoryService artCategoryService) {
        this.artCategoryService = artCategoryService;
    }

    /**
     * [GET /art-categories] Retrieves a list of all art categories.
     * <p>
     * This endpoint is public and used to populate selection fields.
     *
     * @return A {@link List} of {@link ArtCategoryResponse} DTOs, sorted by ID.
     */
    @GetMapping
    public List<ArtCategoryResponse> getAll() {
        return artCategoryService.getAll();
    }

}