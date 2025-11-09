package com.culturacarabobo.sicuc.backend.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.culturacarabobo.sicuc.backend.dtos.ArtCategoryResponse;
import com.culturacarabobo.sicuc.backend.entities.ArtCategory;
import com.culturacarabobo.sicuc.backend.repositories.ArtCategoryRepository;

/**
 * Service layer for managing {@link ArtCategory} entities.
 * <p>
 * This service handles retrieving art categories, primarily for populating
 * form dropdowns.
 */
@Service
public class ArtCategoryService {

    private final ArtCategoryRepository artCategoryRepository;

    /**
     * Constructs the service with the required repository.
     *
     * @param artCategoryRepository Repository for {@link ArtCategory} data access.
     */
    public ArtCategoryService(ArtCategoryRepository artCategoryRepository) {
        this.artCategoryRepository = artCategoryRepository;
    }

    /**
     * Retrieves all art categories, sorted by their ID in ascending order.
     *
     * @return A {@link List} of {@link ArtCategoryResponse} DTOs.
     */
    @SuppressWarnings("null")
    public List<ArtCategoryResponse> getAll() {
        // Find all categories, sort by ID to ensure consistent ordering
        return artCategoryRepository.findAll(Sort.by("id")).stream()
                .map(this::toArtCategoryResponse) // Convert each entity to a DTO
                .collect(Collectors.toList());
    }

    /**
     * Private helper method to convert an {@link ArtCategory} entity to its DTO
     * representation.
     *
     * @param artCategory The {@link ArtCategory} entity to convert.
     * @return The corresponding {@link ArtCategoryResponse} DTO.
     */
    private ArtCategoryResponse toArtCategoryResponse(ArtCategory artCategory) {
        return new ArtCategoryResponse(artCategory.getId(), artCategory.getName());
    }

}