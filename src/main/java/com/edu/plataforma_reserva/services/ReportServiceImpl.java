package com.edu.plataforma_reserva.services;


import com.edu.plataforma_reserva.dtos.DoctorProductivityResponse;
import com.edu.plataforma_reserva.dtos.NoShowPatientResponse;
import com.edu.plataforma_reserva.dtos.OfficeOccupancyResponse;
import com.edu.plataforma_reserva.entities.Doctor;
import com.edu.plataforma_reserva.entities.Office;
import com.edu.plataforma_reserva.entities.Patient;
import com.edu.plataforma_reserva.exeptions.BusinessException;
import com.edu.plataforma_reserva.repositories.AppointmentRepository;
import com.edu.plataforma_reserva.repositories.DoctorRepository;
import com.edu.plataforma_reserva.repositories.OfficeRepository;
import com.edu.plataforma_reserva.repositories.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private final AppointmentRepository appointmentRepository;
    private final OfficeRepository officeRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Override
    public List<OfficeOccupancyResponse> getOfficeOccupancy(LocalDateTime start, LocalDateTime end) {
        validateDateRange(start, end);

        List<Office> offices = officeRepository.findAll(); // o filtrar solo activos si se requiere

        return offices.stream()
                .map(office -> {
                    Long totalAppointments = appointmentRepository.countOfficeOccupancy(office.getId(), start, end);
                    return OfficeOccupancyResponse.builder()
                            .officeId(office.getId())
                            .officeName(office.getName())
                            .totalAppointments(totalAppointments != null ? totalAppointments : 0L)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<DoctorProductivityResponse> getDoctorProductivity() {
        List<Object[]> results = appointmentRepository.getDoctorProductivity();

        return results.stream()
                .map(row -> {
                    UUID doctorId = (UUID) row[0];
                    Long completedCount = (Long) row[1];
                    // Obtener nombre del doctor (puede venir de un mapa cache o consulta adicional)
                    String doctorName = doctorRepository.findById(doctorId)
                            .map(Doctor::getName)
                            .orElse("Desconocido");
                    return DoctorProductivityResponse.builder()
                            .doctorId(doctorId)
                            .doctorName(doctorName)
                            .completedAppointments(completedCount)
                            .build();
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<NoShowPatientResponse> getNoShowPatients(LocalDateTime start, LocalDateTime end) {
        validateDateRange(start, end);

        List<Object[]> results = appointmentRepository.getNoShowPatients(start, end);

        return results.stream()
                .map(row -> {
                    UUID patientId = (UUID) row[0];
                    Long noShowCount = (Long) row[1];
                    String patientName = patientRepository.findById(patientId)
                            .map(Patient::getName)
                            .orElse("Desconocido");
                    return NoShowPatientResponse.builder()
                            .patientId(patientId)
                            .patientName(patientName)
                            .totalNoShows(noShowCount)
                            .build();
                })
                .collect(Collectors.toList());
    }

    private void validateDateRange(LocalDateTime start, LocalDateTime end) {
        if (start == null || end == null) {
            throw new BusinessException("Las fechas de inicio y fin son obligatorias");
        }
        if (start.isAfter(end)) {
            throw new BusinessException("La fecha de inicio no puede ser posterior a la fecha de fin");
        }
    }
}