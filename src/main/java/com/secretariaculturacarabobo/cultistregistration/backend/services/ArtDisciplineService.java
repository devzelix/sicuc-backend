package com.secretariaculturacarabobo.cultistregistration.backend.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.secretariaculturacarabobo.cultistregistration.backend.dtos.ArtDisciplineResponse;
import com.secretariaculturacarabobo.cultistregistration.backend.entities.ArtDiscipline;
import com.secretariaculturacarabobo.cultistregistration.backend.repositories.ArtDisciplineRepository;

/**
 * Service class responsible for operations related to Art Disciplines.
 */
@Service
public class ArtDisciplineService {

    private final ArtDisciplineRepository artDisciplineRepository;

    public ArtDisciplineService(ArtDisciplineRepository artDisciplineRepository) {
        this.artDisciplineRepository = artDisciplineRepository;
    }

    /**
     * Retrieves all art disciplines sorted by ID and maps them to DTOs.
     *
     * @return a list of ArtDisciplineResponse DTOs
     */
    public List<ArtDisciplineResponse> getAll() {
        return artDisciplineRepository.findAll(Sort.by("id")).stream().map(this::toArtDisciplineResponse)
                .collect(Collectors.toList());
    }

    /**
     * Converts an ArtDiscipline entity to its DTO representation.
     *
     * @param artDiscipline the ArtDiscipline entity
     * @return the corresponding ArtDisciplineResponse DTO
     */
    public ArtDisciplineResponse toArtDisciplineResponse(ArtDiscipline artDiscipline) {
        return new ArtDisciplineResponse(artDiscipline.getId(), artDiscipline.getName(),
                artDiscipline.getArtCategory().getId());
    }

}
