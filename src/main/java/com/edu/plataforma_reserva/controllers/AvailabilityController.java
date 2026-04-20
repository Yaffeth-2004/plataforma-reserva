package com.edu.plataforma_reserva.controllers;

import com.edu.plataforma_reserva.dtos.AvailabilitySlotResponse;
import com.edu.plataforma_reserva.services.AvailabilityService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/availability")
@RequiredArgsConstructor
public class AvailabilityController {

    private final AvailabilityService availabilityService;

    @GetMapping
    public ResponseEntity<List<AvailabilitySlotResponse>> getAvailability(
            @RequestParam UUID doctorId,
            @RequestParam UUID appointmentTypeId,
            @RequestParam LocalDate date
    ) {
        List<AvailabilitySlotResponse> response =
                availabilityService.getAvailability(doctorId, appointmentTypeId, date);

        return ResponseEntity.ok(response);
    }
}