package com.secretariaculturacarabobo.cultistregistration.backend.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.secretariaculturacarabobo.cultistregistration.backend.dtos.MunicipalityResponse;
import com.secretariaculturacarabobo.cultistregistration.backend.entities.Municipality;
import com.secretariaculturacarabobo.cultistregistration.backend.repositories.MunicipalityRepository;

@Service
public class MunicipalityService {

    private final MunicipalityRepository municipalityRepository;

    public MunicipalityService(MunicipalityRepository municipalityRepository) {
        this.municipalityRepository = municipalityRepository;
    }

    public List<MunicipalityResponse> getAll() {
        return municipalityRepository.findAll(Sort.by("id")).stream()
                .map(this::toMunicipalityResponse)
                .collect(Collectors.toList());
    }

    public MunicipalityResponse toMunicipalityResponse(Municipality municipality) {
        return new MunicipalityResponse(municipality.getId(), municipality.getName());
    }

}
