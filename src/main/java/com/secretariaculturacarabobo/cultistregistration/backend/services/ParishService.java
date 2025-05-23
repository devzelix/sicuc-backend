package com.secretariaculturacarabobo.cultistregistration.backend.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.secretariaculturacarabobo.cultistregistration.backend.dtos.ParishResponse;
import com.secretariaculturacarabobo.cultistregistration.backend.entities.Parish;
import com.secretariaculturacarabobo.cultistregistration.backend.repositories.ParishRepository;

/**
 * Service class for managing parish-related operations.
 */
@Service
public class ParishService {

    private final ParishRepository parishRepository;

    public ParishService(ParishRepository parishRepository) {
        this.parishRepository = parishRepository;
    }

    /**
     * Retrieves all parishes sorted by ID and maps them to DTOs.
     *
     * @return a list of ParishResponse DTOs
     */
    public List<ParishResponse> getAll() {
        return parishRepository.findAll(Sort.by("id")).stream().map(this::toParishResponse)
                .collect(Collectors.toList());
    }

    /**
     * Converts a Parish entity to its DTO representation.
     *
     * @param parish the Parish entity
     * @return the corresponding ParishResponse DTO
     */
    public ParishResponse toParishResponse(Parish parish) {
        return new ParishResponse(parish.getId(), parish.getName(), parish.getMunicipality().getId());
    }

}
