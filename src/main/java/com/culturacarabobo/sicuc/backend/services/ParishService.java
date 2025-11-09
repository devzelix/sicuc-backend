package com.culturacarabobo.sicuc.backend.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.culturacarabobo.sicuc.backend.dtos.ParishResponse;
import com.culturacarabobo.sicuc.backend.entities.Parish;
import com.culturacarabobo.sicuc.backend.repositories.ParishRepository;

/**
 * Service layer for managing {@link Parish} entities.
 * <p>
 * This service handles retrieving parishes, primarily for populating
 * form dropdowns.
 */
@Service
public class ParishService {

    private final ParishRepository parishRepository;

    /**
     * Constructs the service with the required repository.
     *
     * @param parishRepository Repository for {@link Parish} data access.
     */
    public ParishService(ParishRepository parishRepository) {
        this.parishRepository = parishRepository;
    }

    /**
     * Retrieves all parishes, sorted by their ID in ascending order.
     *
     * @return A {@link List} of {@link ParishResponse} DTOs.
     */
    @SuppressWarnings("null")
    public List<ParishResponse> getAll() {
        // Find all parishes, sort by ID to ensure consistent ordering
        return parishRepository.findAll(Sort.by("id")).stream()
                .map(this::toParishResponse) // Convert each entity to a DTO
                .collect(Collectors.toList());
    }

    /**
     * Private helper method to convert a {@link Parish} entity to its DTO
     * representation.
     *
     * @param parish The {@link Parish} entity to convert.
     * @return The corresponding {@link ParishResponse} DTO.
     */
    private ParishResponse toParishResponse(Parish parish) {
        return new ParishResponse(
                parish.getId(),
                parish.getName(),
                parish.getMunicipality().getId()
        );
    }
}