package com.culturacarabobo.sicuc.backend.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.culturacarabobo.sicuc.backend.dtos.ArtCategoryResponse;
import com.culturacarabobo.sicuc.backend.services.ArtCategoryService;

import org.springframework.web.bind.annotation.GetMapping;

/**
 * REST controller responsible for handling requests related to art categories.
 * Exposes endpoints to retrieve art category data.
 */
@RestController
@RequestMapping("/art-categories")
public class ArtCategoryController {

    private final ArtCategoryService artCategoryService;

    /**
     * Constructor for dependency injection of ArtCategoryService.
     *
     * @param artCategoryService service to manage art category data
     */
    public ArtCategoryController(ArtCategoryService artCategoryService) {
        this.artCategoryService = artCategoryService;
    }

    /**
     * Handles GET requests to retrieve all art categories.
     *
     * @return list of ArtCategoryResponse DTOs containing art category information
     */
    @GetMapping
    public List<ArtCategoryResponse> getAll() {
        return artCategoryService.getAll();
    }

}
