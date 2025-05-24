package com.culturacarabobo.cultorregistration.backend.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.culturacarabobo.cultorregistration.backend.dtos.ParishResponse;
import com.culturacarabobo.cultorregistration.backend.services.ParishService;

import org.springframework.web.bind.annotation.GetMapping;

/**
 * REST controller to manage parish-related endpoints.
 * Provides access to parish data via HTTP requests.
 */
@RestController
@RequestMapping("/parishes")
public class ParishController {

    private final ParishService parishService;

    /**
     * Constructor-based dependency injection for ParishService.
     *
     * @param parishService service handling parish data operations
     */
    public ParishController(ParishService parishService) {
        this.parishService = parishService;
    }

    /**
     * Handles HTTP GET requests for retrieving all parishes.
     *
     * @return a list of ParishResponse DTOs representing all parishes
     */
    @GetMapping
    public List<ParishResponse> getAll() {
        return parishService.getAll();
    }

}
