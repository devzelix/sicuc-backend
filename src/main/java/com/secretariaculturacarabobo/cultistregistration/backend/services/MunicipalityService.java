package com.secretariaculturacarabobo.cultistregistration.backend.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.secretariaculturacarabobo.cultistregistration.backend.dtos.MunicipalityResponse;
import com.secretariaculturacarabobo.cultistregistration.backend.entities.Municipality;
import com.secretariaculturacarabobo.cultistregistration.backend.repositories.MunicipalityRepository;

/**
 * Service class for managing municipality-related operations.
 */
@Service
public class MunicipalityService {

    private final MunicipalityRepository municipalityRepository;

    public MunicipalityService(MunicipalityRepository municipalityRepository) {
        this.municipalityRepository = municipalityRepository;
    }

    /**
     * Retrieves all municipalities sorted by ID and maps them to DTOs.
     *
     * @return a list of MunicipalityResponse DTOs
     */
    public List<MunicipalityResponse> getAll() {
        return municipalityRepository.findAll(Sort.by("id")).stream()
                .map(this::toMunicipalityResponse)
                .collect(Collectors.toList());
    }

    /**
     * Converts a Municipality entity to its DTO representation.
     *
     * @param municipality the Municipality entity
     * @return the corresponding MunicipalityResponse DTO
     */
    public MunicipalityResponse toMunicipalityResponse(Municipality municipality) {
        return new MunicipalityResponse(municipality.getId(), municipality.getName());
    }

}
