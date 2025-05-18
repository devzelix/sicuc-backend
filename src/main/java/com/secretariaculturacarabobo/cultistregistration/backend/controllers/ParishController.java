package com.secretariaculturacarabobo.cultistregistration.backend.controllers;

import java.util.List;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.secretariaculturacarabobo.cultistregistration.backend.dtos.ParishResponse;
import com.secretariaculturacarabobo.cultistregistration.backend.services.ParishService;

import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/parishes")
public class ParishController {

    private final ParishService parishService;

    public ParishController(ParishService parishService) {
        this.parishService = parishService;
    }

    @GetMapping
    public List<ParishResponse> getAll() {
        return parishService.getAll();
    }

}
