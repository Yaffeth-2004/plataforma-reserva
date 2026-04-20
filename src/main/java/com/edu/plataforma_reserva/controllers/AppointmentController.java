package com.edu.plataforma_reserva.controllers;

import com.edu.plataforma_reserva.dtos.AppointmentResponse;
import com.edu.plataforma_reserva.dtos.CancelAppointmentRequest;
import com.edu.plataforma_reserva.dtos.CreateAppointmentRequest;
import com.edu.plataforma_reserva.services.AppointmentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/appointments")
@RequiredArgsConstructor
public class AppointmentController {

    private final AppointmentService appointmentService;

    // ✅ Crear cita
    @PostMapping
    public ResponseEntity<AppointmentResponse> createAppointment(
            @Valid @RequestBody CreateAppointmentRequest request
    ) {
        AppointmentResponse response = appointmentService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // ✅ Obtener por ID
    @GetMapping("/{id}")
    public ResponseEntity<AppointmentResponse> getById(
            @PathVariable UUID id
    ) {
        AppointmentResponse response = appointmentService.findById(id);
        return ResponseEntity.ok(response);
    }

    // ✅ Obtener todas
    @GetMapping
    public ResponseEntity<List<AppointmentResponse>> getAll() {
        List<AppointmentResponse> response = appointmentService.findAll();
        return ResponseEntity.ok(response);
    }

    // ✅ Confirmar cita
    @PutMapping("/{id}/confirm")
    public ResponseEntity<AppointmentResponse> confirmAppointment(
            @PathVariable UUID id
    ) {
        AppointmentResponse response = appointmentService.confirm(id);
        return ResponseEntity.ok(response);
    }

    // ✅ Cancelar cita
    @PutMapping("/{id}/cancel")
    public ResponseEntity<AppointmentResponse> cancelAppointment(
            @PathVariable UUID id,
            @Valid @RequestBody CancelAppointmentRequest request
    ) {
        AppointmentResponse response = appointmentService.cancel(id, request);
        return ResponseEntity.ok(response);
    }

    // ✅ Completar cita
    @PutMapping("/{id}/complete")
    public ResponseEntity<AppointmentResponse> completeAppointment(
            @PathVariable UUID id,
            @RequestParam(required = false) String observations
    ) {
        AppointmentResponse response = appointmentService.complete(id, observations);
        return ResponseEntity.ok(response);
    }

    // ✅ Marcar como no asistió
    @PutMapping("/{id}/no-show")
    public ResponseEntity<AppointmentResponse> markNoShow(
            @PathVariable UUID id
    ) {
        AppointmentResponse response = appointmentService.noShow(id);
        return ResponseEntity.ok(response);
    }
}