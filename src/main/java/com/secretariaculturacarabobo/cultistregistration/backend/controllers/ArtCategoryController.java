package com.secretariaculturacarabobo.cultistregistration.backend.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.secretariaculturacarabobo.cultistregistration.backend.dtos.ArtCategoryResponse;
import com.secretariaculturacarabobo.cultistregistration.backend.services.ArtCategoryService;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

@CrossOrigin(origins = "http://192.168.0.190:5173")
@RestController
@RequestMapping("/art-categories")
public class ArtCategoryController {

    private final ArtCategoryService artCategoryService;

    public ArtCategoryController(ArtCategoryService artCategoryService) {
        this.artCategoryService = artCategoryService;
    }

    @GetMapping
    public List<ArtCategoryResponse> getAll() {
        return artCategoryService.getAll();
    }

}
