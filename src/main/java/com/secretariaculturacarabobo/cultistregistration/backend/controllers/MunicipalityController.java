package com.secretariaculturacarabobo.cultistregistration.backend.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.secretariaculturacarabobo.cultistregistration.backend.dtos.MunicipalityResponse;
import com.secretariaculturacarabobo.cultistregistration.backend.services.MunicipalityService;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/municipalities")
public class MunicipalityController {

    private final MunicipalityService municipalityService;

    public MunicipalityController(MunicipalityService municipalityService) {
        this.municipalityService = municipalityService;
    }

    @GetMapping
    public List<MunicipalityResponse> getAll() {
        return municipalityService.getAll();
    }

}
