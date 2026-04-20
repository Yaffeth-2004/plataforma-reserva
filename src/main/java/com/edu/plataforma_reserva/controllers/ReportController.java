package com.edu.plataforma_reserva.controllers;


import com.edu.plataforma_reserva.dtos.DoctorProductivityResponse;
import com.edu.plataforma_reserva.dtos.NoShowPatientResponse;
import com.edu.plataforma_reserva.dtos.OfficeOccupancyResponse;
import com.edu.plataforma_reserva.services.ReportService;

import lombok.RequiredArgsConstructor;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    // ✅ 1. Ocupación de consultorios
    @GetMapping("/offices/occupancy")
    public ResponseEntity<List<OfficeOccupancyResponse>> getOfficeOccupancy(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime start,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime end
    ) {
        List<OfficeOccupancyResponse> response =
                reportService.getOfficeOccupancy(start, end);

        return ResponseEntity.ok(response);
    }

    // ✅ 2. Productividad de doctores
    @GetMapping("/doctors/productivity")
    public ResponseEntity<List<DoctorProductivityResponse>> getDoctorProductivity() {

        List<DoctorProductivityResponse> response =
                reportService.getDoctorProductivity();

        return ResponseEntity.ok(response);
    }

    // ✅ 3. Pacientes con más no-show
    @GetMapping("/patients/no-show")
    public ResponseEntity<List<NoShowPatientResponse>> getNoShowPatients(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime start,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime end
    ) {
        List<NoShowPatientResponse> response =
                reportService.getNoShowPatients(start, end);

        return ResponseEntity.ok(response);
    }
}