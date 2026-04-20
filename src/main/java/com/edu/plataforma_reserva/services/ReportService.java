package com.edu.plataforma_reserva.services;


import com.edu.plataforma_reserva.dtos.DoctorProductivityResponse;
import com.edu.plataforma_reserva.dtos.NoShowPatientResponse;
import com.edu.plataforma_reserva.dtos.OfficeOccupancyResponse;

import java.time.LocalDateTime;
import java.util.List;

public interface ReportService {

    /**
     * Ocupación de consultorios en un rango de fechas.
     * Retorna lista con todos los consultorios y el total de citas (SCHEDULED, CONFIRMED, COMPLETED)
     * dentro del rango.
     */
    List<OfficeOccupancyResponse> getOfficeOccupancy(LocalDateTime start, LocalDateTime end);

    /**
     * Productividad de doctores: número de citas COMPLETED por doctor,
     * ordenado descendente.
     */
    List<DoctorProductivityResponse> getDoctorProductivity();

    /**
     * Pacientes con mayor número de NO_SHOW en un período.
     * Retorna lista ordenada descendente por cantidad de no asistencias.
     */
    List<NoShowPatientResponse> getNoShowPatients(LocalDateTime start, LocalDateTime end);
}
