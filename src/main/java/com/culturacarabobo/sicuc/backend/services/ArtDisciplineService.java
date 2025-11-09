package com.culturacarabobo.sicuc.backend.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.culturacarabobo.sicuc.backend.dtos.ArtDisciplineResponse;
import com.culturacarabobo.sicuc.backend.entities.ArtDiscipline;
import com.culturacarabobo.sicuc.backend.repositories.ArtDisciplineRepository;

/**
 * Service layer for managing {@link ArtDiscipline} entities.
 * <p>
 * This service handles retrieving art disciplines, primarily for populating
 * form dropdowns.
 */
@Service
public class ArtDisciplineService {

    private final ArtDisciplineRepository artDisciplineRepository;

    /**
     * Constructs the service with the required repository.
     *
     * @param artDisciplineRepository Repository for {@link ArtDiscipline} data
     * access.
     */
    public ArtDisciplineService(ArtDisciplineRepository artDisciplineRepository) {
        this.artDisciplineRepository = artDisciplineRepository;
    }

    /**
     * Retrieves all art disciplines, sorted by their ID in ascending order.
     *
     * @return A {@link List} of {@link ArtDisciplineResponse} DTOs.
     */
    @SuppressWarnings("null")
    public List<ArtDisciplineResponse> getAll() {
        // Find all disciplines, sort by ID to ensure consistent ordering
        return artDisciplineRepository.findAll(Sort.by("id")).stream()
                .map(this::toArtDisciplineResponse) // Convert each entity to a DTO
                .collect(Collectors.toList());
    }

    /**
     * Private helper method to convert an {@link ArtDiscipline} entity to its DTO
     * representation.
     *
     * @param artDiscipline The {@link ArtDiscipline} entity to convert.
     * @return The corresponding {@link ArtDisciplineResponse} DTO.
     */
    private ArtDisciplineResponse toArtDisciplineResponse(ArtDiscipline artDiscipline) {
        return new ArtDisciplineResponse(
                artDiscipline.getId(),
                artDiscipline.getName(),
                artDiscipline.getArtCategory().getId()
        );
    }
}