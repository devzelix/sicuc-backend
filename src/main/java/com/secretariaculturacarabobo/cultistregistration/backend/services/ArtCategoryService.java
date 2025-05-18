package com.secretariaculturacarabobo.cultistregistration.backend.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.secretariaculturacarabobo.cultistregistration.backend.dtos.ArtCategoryResponse;
import com.secretariaculturacarabobo.cultistregistration.backend.entities.ArtCategory;
import com.secretariaculturacarabobo.cultistregistration.backend.repositories.ArtCategoryRepository;

@Service
public class ArtCategoryService {

    private final ArtCategoryRepository artCategoryRepository;

    public ArtCategoryService(ArtCategoryRepository artCategoryRepository) {
        this.artCategoryRepository = artCategoryRepository;
    }

    public List<ArtCategoryResponse> getAll() {
        return artCategoryRepository.findAll(Sort.by("id")).stream()
                .map(this::toArtCategoryResponse)
                .collect(Collectors.toList());
    }

    public ArtCategoryResponse toArtCategoryResponse(ArtCategory artCategory) {
        return new ArtCategoryResponse(artCategory.getId(), artCategory.getName());
    }

}
