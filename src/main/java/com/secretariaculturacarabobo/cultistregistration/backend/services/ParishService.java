package com.secretariaculturacarabobo.cultistregistration.backend.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.secretariaculturacarabobo.cultistregistration.backend.dtos.ParishResponse;
import com.secretariaculturacarabobo.cultistregistration.backend.entities.Parish;
import com.secretariaculturacarabobo.cultistregistration.backend.repositories.ParishRepository;

@Service
public class ParishService {

    private final ParishRepository parishRepository;

    public ParishService(ParishRepository parishRepository) {
        this.parishRepository = parishRepository;
    }

    public List<ParishResponse> getAll() {
        return parishRepository.findAll(Sort.by("id")).stream().map(this::toParishResponse)
                .collect(Collectors.toList());
    }

    public ParishResponse toParishResponse(Parish parish) {
        return new ParishResponse(parish.getId(), parish.getName(), parish.getMunicipality().getId());
    }

}
