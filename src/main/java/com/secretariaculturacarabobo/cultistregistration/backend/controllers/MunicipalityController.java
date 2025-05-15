package com.secretariaculturacarabobo.cultistregistration.backend.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.secretariaculturacarabobo.cultistregistration.backend.entities.Municipality;
import com.secretariaculturacarabobo.cultistregistration.backend.repositories.MunicipalityRepository;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/municipalities")
public class MunicipalityController {

    @Autowired
    private MunicipalityRepository municipalityRepository;

    @GetMapping
    public List<Municipality> getAllMunicipalities() {
        return municipalityRepository.findAll();
    }

    @PostMapping
    public Municipality createMunicipality(@Valid @RequestBody Municipality municipality) {
        return municipalityRepository.save(municipality);
    }

}
