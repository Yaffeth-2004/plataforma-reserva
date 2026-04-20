package com.edu.plataforma_reserva.controllers;


import com.edu.plataforma_reserva.dtos.CreateOfficeRequest;
import com.edu.plataforma_reserva.dtos.UpdateOfficeRequest;
import com.edu.plataforma_reserva.dtos.OfficeResponse;
import com.edu.plataforma_reserva.services.OfficeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/offices")
@RequiredArgsConstructor
public class OfficeController {

    private final OfficeService officeService;

    // ✅ Crear consultorio
    @PostMapping
    public ResponseEntity<OfficeResponse> createOffice(
            @Valid @RequestBody CreateOfficeRequest request
    ) {
        OfficeResponse response = officeService.createOffice(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ✅ Obtener todos los consultorios
    @GetMapping
    public ResponseEntity<List<OfficeResponse>> getAllOffices() {
        List<OfficeResponse> response = officeService.getAllOffices();
        return ResponseEntity.ok(response);
    }

    // ✅ Actualizar consultorio (incluye activar/inactivar)
    @PutMapping("/{id}")
    public ResponseEntity<OfficeResponse> updateOffice(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateOfficeRequest request
    ) {
        OfficeResponse response = officeService.updateOffice(id, request);
        return ResponseEntity.ok(response);
    }
}
