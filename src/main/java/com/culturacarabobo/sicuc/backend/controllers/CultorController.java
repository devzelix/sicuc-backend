package com.culturacarabobo.sicuc.backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.culturacarabobo.sicuc.backend.dtos.CultorRequest;
import com.culturacarabobo.sicuc.backend.dtos.CultorResponse;
import com.culturacarabobo.sicuc.backend.services.CultorService;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * REST controller to manage Cultor entities.
 * Supports filtering and creation of cultors via HTTP endpoints.
 */
@RestController
@RequestMapping("/cultors")
public class CultorController {

    private final CultorService cultorService;

    /**
     * Constructor for dependency injection of the CultorService.
     * 
     * @param cultorService Service that handles business logic for cultors.
     */
    public CultorController(CultorService cultorService) {
        this.cultorService = cultorService;
    }

    /**
     * GET endpoint to retrieve a list of cultors filtered by optional parameters.
     * 
     * @param query           Optional search query to match cultor names, IDs,
     *                        etc.
     * @param gender          Optional filter by gender.
     * @param municipalityId  Optional filter by municipality ID.
     * @param parishId        Optional filter by parish ID.
     * @param artCategoryId   Optional filter by art category ID.
     * @param artDisciplineId Optional filter by art discipline ID.
     * @param hasDisability   Optional filter by presence of disability.
     * @param hasIllness      Optional filter by presence of illness.
     * @return List of CultorResponse DTOs matching the filters.
     */
    @SuppressWarnings("null")
    @GetMapping
    public List<CultorResponse> getCultorsFiltered(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Integer municipalityId,
            @RequestParam(required = false) Integer parishId,
            @RequestParam(required = false) Integer artCategoryId,
            @RequestParam(required = false) Integer artDisciplineId,
            @RequestParam(required = false) Boolean hasDisability,
            @RequestParam(required = false) Boolean hasIllness) {
        return cultorService.getAllCultorsWithFilters(query,
                gender, municipalityId, parishId, artCategoryId, artDisciplineId, hasDisability, hasIllness);
    }

    /**
     * POST endpoint to create a new cultor.
     * Validates the request body before passing it to the service layer.
     * 
     * @param cultorRequest DTO containing the data to create a cultor.
     * @return ResponseEntity with the created CultorResponse and appropriate HTTP
     *         status.
     */
    @SuppressWarnings("null")
    @PostMapping
    public ResponseEntity<CultorResponse> create(@Valid @RequestBody CultorRequest cultorRequest) {
        return cultorService.create(cultorRequest);
    }

    /**
     * NUEVO ENDPOINT:
     * PUT endpoint para actualizar un cultor existente por su ID.
     *
     * @param id            El ID del cultor a actualizar (viene de la URL).
     * @param cultorRequest DTO con los datos para actualizar.
     * @return ResponseEntity con el CultorResponse actualizado.
     */
    @SuppressWarnings("null")
    @PutMapping("/{id}")
    public ResponseEntity<CultorResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody CultorRequest cultorRequest) {
        
        return cultorService.update(id, cultorRequest);
    }

    /**
     * DELETE endpoint para eliminar un cultor por su ID.
     *
     * @param id El ID del cultor a eliminar (viene de la URL).
     * @return ResponseEntity con el status apropiado (204 o 404).
     */
    @SuppressWarnings("null")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        return cultorService.delete(id);
    }

}
