package com.secretariaculturacarabobo.cultistregistration.backend.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.secretariaculturacarabobo.cultistregistration.backend.entities.Cultist;
import com.secretariaculturacarabobo.cultistregistration.backend.repositories.CultistRepository;

import jakarta.validation.Valid;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/cultists")
public class CultistController {

    @Autowired
    private CultistRepository cultistRepository;

    @GetMapping
    public List<Cultist> getAllCultists() {
        return cultistRepository.findAll();
    }

    @GetMapping("/{id}")
    public Cultist getCultistById(@PathVariable Integer id) {
        return cultistRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cultor no encontrado"));
    }

    @PostMapping
    public Cultist createCultist(@Valid @RequestBody Cultist cultist) {
        return cultistRepository.save(cultist);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCultistById(@PathVariable Integer id) {
        Cultist cultist = cultistRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cultor no encontrado"));
        cultistRepository.delete(cultist);
        return ResponseEntity.noContent().build();
    }

}
