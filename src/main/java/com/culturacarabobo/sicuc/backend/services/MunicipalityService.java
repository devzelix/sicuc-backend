package com.culturacarabobo.sicuc.backend.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.culturacarabobo.sicuc.backend.dtos.MunicipalityResponse;
import com.culturacarabobo.sicuc.backend.entities.Municipality;
import com.culturacarabobo.sicuc.backend.repositories.MunicipalityRepository;

/**
 * Service layer for managing {@link Municipality} entities.
 * <p>
 * This service handles retrieving municipalities, primarily for populating
 * form dropdowns.
 */
@Service
public class MunicipalityService {

    private final MunicipalityRepository municipalityRepository;

    /**
     * Constructs the service with the required repository.
     *
     * @param municipalityRepository Repository for {@link Municipality} data access.
     */
    public MunicipalityService(MunicipalityRepository municipalityRepository) {
        this.municipalityRepository = municipalityRepository;
    }

    /**
     * Retrieves all municipalities, sorted by their ID in ascending order.
     *
     * @return A {@link List} of {@link MunicipalityResponse} DTOs.
     */
    @SuppressWarnings("null")
    public List<MunicipalityResponse> getAll() {
        // Find all municipalities, sort by ID to ensure consistent ordering
        return municipalityRepository.findAll(Sort.by("id")).stream()
                .map(this::toMunicipalityResponse) // Convert each entity to a DTO
                .collect(Collectors.toList());
    }

    /**
     * Private helper method to convert a {@link Municipality} entity to its DTO
     * representation.
     *
     * @param municipality The {@link Municipality} entity to convert.
     * @return The corresponding {@link MunicipalityResponse} DTO.
     */
    private MunicipalityResponse toMunicipalityResponse(Municipality municipality) {
        return new MunicipalityResponse(municipality.getId(), municipality.getName());
    }

}