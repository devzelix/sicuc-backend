package com.culturacarabobo.sicuc.backend.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.culturacarabobo.sicuc.backend.dtos.ArtCategoryResponse;
import com.culturacarabobo.sicuc.backend.entities.ArtCategory;
import com.culturacarabobo.sicuc.backend.repositories.ArtCategoryRepository;

/**
 * Service class responsible for operations related to Art Categories.
 */
@Service
public class ArtCategoryService {

    private final ArtCategoryRepository artCategoryRepository;

    public ArtCategoryService(ArtCategoryRepository artCategoryRepository) {
        this.artCategoryRepository = artCategoryRepository;
    }

    /**
     * Retrieves all art categories sorted by ID and maps them to DTOs.
     *
     * @return a list of ArtCategoryResponse DTOs
     */
    @SuppressWarnings("null")
    public List<ArtCategoryResponse> getAll() {
        return artCategoryRepository.findAll(Sort.by("id")).stream()
                .map(this::toArtCategoryResponse)
                .collect(Collectors.toList());
    }

    /**
     * Converts an ArtCategory entity to its DTO representation.
     *
     * @param artCategory the ArtCategory entity
     * @return the corresponding ArtCategoryResponse DTO
     */
    public ArtCategoryResponse toArtCategoryResponse(ArtCategory artCategory) {
        return new ArtCategoryResponse(artCategory.getId(), artCategory.getName());
    }

}
