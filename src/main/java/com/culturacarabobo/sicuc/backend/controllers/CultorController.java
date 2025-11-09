package com.culturacarabobo.sicuc.backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.culturacarabobo.sicuc.backend.dtos.CultorRequest;
import com.culturacarabobo.sicuc.backend.dtos.CultorResponse;
import com.culturacarabobo.sicuc.backend.services.CultorService;

import jakarta.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * REST controller that exposes endpoints for the Cultor entity CRUD operations.
 * <p>
 * Delegates all business logic to {@link CultorService}.
 */
@RestController
@RequestMapping("/cultors")
public class CultorController {

    private final CultorService cultorService;

    /**
     * Constructs the controller with the required CultorService.
     *
     * @param cultorService The service responsible for cultor business logic.
     */
    public CultorController(CultorService cultorService) {
        this.cultorService = cultorService;
    }

    /**
     * [GET /cultors] Retrieves a paginated and filtered list of cultors.
     * <p>
     * Supports dynamic filtering by multiple criteria and provides paginated results.
     *
     * @param query           Optional search term for name, ID number, or phone.
     * @param gender          Optional filter for gender ("M" or "F").
     * @param municipalityId  Optional filter by municipality ID.
     * @param parishId        Optional filter by parish ID.
     * @param artCategoryId   Optional filter by art category ID.
     * @param artDisciplineId Optional filter by art discipline ID.
     * @param hasDisability   Optional filter for disability status (true/false).
     * @param hasIllness      Optional filter for illness status (true/false).
     * @param pageable        Automatic Spring parameter for pagination (e.g., ?page=0&size=10&sort=lastName,asc).
     * @return A {@link Page} of {@link CultorResponse} DTOs matching the filters.
     */
    @SuppressWarnings("null")
    @GetMapping
    public Page<CultorResponse> getCultorsFiltered(
            @RequestParam(required = false) String query,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) Integer municipalityId,
            @RequestParam(required = false) Integer parishId,
            @RequestParam(required = false) Integer artCategoryId,
            @RequestParam(required = false) Integer artDisciplineId,
            @RequestParam(required = false) Boolean hasDisability,
            @RequestParam(required = false) Boolean hasIllness,
            Pageable pageable) {

        return cultorService.getAllCultorsWithFilters(query,
                gender, municipalityId, parishId, artCategoryId, artDisciplineId, hasDisability, hasIllness,
                pageable);
    }

    /**
     * [GET /cultors/{id}] Retrieves a single cultor by its ID.
     *
     * @param id The ID (primary key) of the cultor to retrieve.
     * @return A {@link ResponseEntity} with the {@link CultorResponse} and HTTP status 200 (OK).
     * @throws jakarta.persistence.EntityNotFoundException If the ID is not found (handled by GlobalExceptionHandler as 404).
     */
    @SuppressWarnings("null")
    @GetMapping("/{id}")
    public ResponseEntity<CultorResponse> getById(@PathVariable Integer id) {
        return cultorService.getById(id);
    }

    /**
     * [POST /cultors] Creates a new cultor.
     * <p>
     * Validates the incoming {@link CultorRequest} body.
     *
     * @param cultorRequest The DTO containing the data for the new cultor. Must be valid.
     * @return A {@link ResponseEntity} with the created {@link CultorResponse} and HTTP status 201 (Created).
     * @throws org.springframework.web.bind.MethodArgumentNotValidException If DTO validation fails (handled by GlobalExceptionHandler as 400).
     * @throws com.culturacarabobo.sicuc.backend.exceptions.DuplicateEntityException If a unique constraint (idNumber, phone, etc.) is violated (handled by GlobalExceptionHandler as 409).
     */
    @SuppressWarnings("null")
    @PostMapping
    public ResponseEntity<CultorResponse> create(@Valid @RequestBody CultorRequest cultorRequest) {
        return cultorService.create(cultorRequest);
    }

    /**
     * [PUT /cultors/{id}] Updates an existing cultor by its ID.
     * <p>
     * Validates the incoming {@link CultorRequest} body and checks business rules
     * (e.g., immutable fields, unique constraints).
     *
     * @param id            The ID of the cultor to update.
     * @param cultorRequest The DTO with the updated data. Must be valid.
     * @return A {@link ResponseEntity} with the updated {@link CultorResponse} and HTTP status 200 (OK).
     * @throws jakarta.persistence.EntityNotFoundException If the ID is not found (returns 404).
     * @throws org.springframework.web.bind.MethodArgumentNotValidException If DTO validation fails (returns 400).
     * @throws com.culturacarabobo.sicuc.backend.exceptions.DuplicateEntityException If a unique field (e.g., phone) conflicts with another cultor (returns 409).
     * @throws java.lang.IllegalArgumentException If an immutable field (e.g., idNumber) is changed (returns 400).
     */
    @SuppressWarnings("null")
    @PutMapping("/{id}")
    public ResponseEntity<CultorResponse> update(
            @PathVariable Integer id,
            @Valid @RequestBody CultorRequest cultorRequest) {
        
        return cultorService.update(id, cultorRequest);
    }

    /**
     * [DELETE /cultors/{id}] Deletes a cultor by its ID.
     *
     * @param id The ID of the cultor to delete.
     * @return A {@link ResponseEntity} with HTTP status 204 (No Content).
     * @throws jakarta.persistence.EntityNotFoundException If the ID is not found (returns 404).
     */
    @SuppressWarnings("null")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        return cultorService.delete(id);
    }
}