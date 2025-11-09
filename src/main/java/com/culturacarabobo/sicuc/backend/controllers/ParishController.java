package com.culturacarabobo.sicuc.backend.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.culturacarabobo.sicuc.backend.dtos.ParishResponse;
import com.culturacarabobo.sicuc.backend.services.ParishService;

import org.springframework.web.bind.annotation.GetMapping;

/**
 * REST controller for exposing read-only endpoints related to Parishes.
 * <p>
 * This controller is primarily used to provide data for frontend form
 * dropdowns.
 * It delegates all logic to {@link ParishService}.
 */
@RestController
@RequestMapping("/parishes")
public class ParishController {

    private final ParishService parishService;

    /**
     * Constructs the controller with the required ParishService.
     *
     * @param parishService The service responsible for parish business logic.
     */
    public ParishController(ParishService parishService) {
        this.parishService = parishService;
    }

    /**
     * [GET /parishes] Retrieves a list of all parishes.
     * <p>
     * This endpoint is public and used to populate selection fields.
     *
     * @return A {@link List} of {@link ParishResponse} DTOs, sorted by ID.
     */
    @GetMapping
    public List<ParishResponse> getAll() {
        return parishService.getAll();
    }

}