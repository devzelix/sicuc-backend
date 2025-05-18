package com.secretariaculturacarabobo.cultistregistration.backend.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.secretariaculturacarabobo.cultistregistration.backend.dtos.ArtDisciplineResponse;
import com.secretariaculturacarabobo.cultistregistration.backend.services.ArtDisciplineService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@CrossOrigin(origins = "http://192.168.0.190:5173")
@RestController
@RequestMapping("/art-disciplines")
public class ArtDisciplineController {

    private final ArtDisciplineService artDisciplineService;

    public ArtDisciplineController(ArtDisciplineService artDisciplineService) {
        this.artDisciplineService = artDisciplineService;
    }

    @GetMapping
    public List<ArtDisciplineResponse> getAll() {
        return artDisciplineService.getAll();
    }

}
