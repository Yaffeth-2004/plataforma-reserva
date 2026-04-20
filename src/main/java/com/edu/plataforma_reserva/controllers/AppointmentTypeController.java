package com.edu.plataforma_reserva.controllers;

import com.edu.plataforma_reserva.dtos.CreateAppointmentTypeRequest;
import com.edu.plataforma_reserva.dtos.AppointmentTypeResponse;
import com.edu.plataforma_reserva.services.AppointmentTypeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointment-types")
@RequiredArgsConstructor
public class AppointmentTypeController {

    private final AppointmentTypeService appointmentTypeService;

    // ✅ Crear tipo de cita
    @PostMapping
    public ResponseEntity<AppointmentTypeResponse> createAppointmentType(
            @Valid @RequestBody CreateAppointmentTypeRequest request
    ) {
        AppointmentTypeResponse response = appointmentTypeService.createAppointmentType(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ✅ Obtener todos los tipos de cita
    @GetMapping
    public ResponseEntity<List<AppointmentTypeResponse>> getAllAppointmentTypes() {
        List<AppointmentTypeResponse> response = appointmentTypeService.getAllAppointmentTypes();
        return ResponseEntity.ok(response);
    }
}
