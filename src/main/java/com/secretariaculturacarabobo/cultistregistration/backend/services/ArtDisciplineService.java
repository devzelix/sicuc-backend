package com.secretariaculturacarabobo.cultistregistration.backend.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.secretariaculturacarabobo.cultistregistration.backend.dtos.ArtDisciplineResponse;
import com.secretariaculturacarabobo.cultistregistration.backend.entities.ArtDiscipline;
import com.secretariaculturacarabobo.cultistregistration.backend.repositories.ArtDisciplineRepository;

@Service
public class ArtDisciplineService {

    private final ArtDisciplineRepository artDisciplineRepository;

    public ArtDisciplineService(ArtDisciplineRepository artDisciplineRepository) {
        this.artDisciplineRepository = artDisciplineRepository;
    }

    public List<ArtDisciplineResponse> getAll() {
        return artDisciplineRepository.findAll(Sort.by("id")).stream().map(this::toArtDisciplineResponse)
                .collect(Collectors.toList());
    }

    public ArtDisciplineResponse toArtDisciplineResponse(ArtDiscipline artDiscipline) {
        return new ArtDisciplineResponse(artDiscipline.getId(), artDiscipline.getName(),
                artDiscipline.getaArtCategory().getId());
    }

}
