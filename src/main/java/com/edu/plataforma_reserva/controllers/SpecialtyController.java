package com.edu.plataforma_reserva.controllers;


import com.edu.plataforma_reserva.dtos.CreateSpecialtyRequest;
import com.edu.plataforma_reserva.dtos.SpecialtyResponse;
import com.edu.plataforma_reserva.services.SpecialtyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/specialties")
@RequiredArgsConstructor
public class SpecialtyController {

    private final SpecialtyService specialtyService;

    // ✅ Crear especialidad
    @PostMapping
    public ResponseEntity<SpecialtyResponse> createSpecialty(
            @Valid @RequestBody CreateSpecialtyRequest request
    ) {
        SpecialtyResponse response = specialtyService.createSpecialty(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ✅ Obtener todas las especialidades
    @GetMapping
    public ResponseEntity<List<SpecialtyResponse>> getAllSpecialties() {
        List<SpecialtyResponse> response = specialtyService.getAllSpecialties();
        return ResponseEntity.ok(response);
    }
}