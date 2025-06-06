package com.culturacarabobo.sicuc.backend.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.culturacarabobo.sicuc.backend.dtos.ArtDisciplineResponse;
import com.culturacarabobo.sicuc.backend.services.ArtDisciplineService;

import org.springframework.web.bind.annotation.GetMapping;

/**
 * REST controller to handle HTTP requests related to Art Disciplines.
 * Provides endpoints to retrieve art discipline data.
 */
@RestController
@RequestMapping("/art-disciplines")
public class ArtDisciplineController {

    private final ArtDisciplineService artDisciplineService;

    /**
     * Constructor to inject the ArtDisciplineService dependency.
     * 
     * @param artDisciplineService service layer for art discipline operations
     */
    public ArtDisciplineController(ArtDisciplineService artDisciplineService) {
        this.artDisciplineService = artDisciplineService;
    }

    /**
     * GET endpoint to retrieve a list of all art disciplines.
     * 
     * @return list of ArtDisciplineResponse DTOs containing art discipline
     *         information
     */
    @GetMapping
    public List<ArtDisciplineResponse> getAll() {
        return artDisciplineService.getAll();
    }

}
