package com.edu.plataforma_reserva.services;

import com.edu.plataforma_reserva.dtos.AvailabilitySlotResponse;
import com.edu.plataforma_reserva.entities.Appointment;
import com.edu.plataforma_reserva.entities.AppointmentType;
import com.edu.plataforma_reserva.entities.DoctorSchedule;
import com.edu.plataforma_reserva.enums.AppointmentStatus;
import com.edu.plataforma_reserva.exeptions.ResourceNotFoundException;
import com.edu.plataforma_reserva.repositories.AppointmentRepository;
import com.edu.plataforma_reserva.repositories.AppointmentTypeRepository;
import com.edu.plataforma_reserva.repositories.DoctorScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService {

    private final DoctorScheduleRepository doctorScheduleRepository;
    private final AppointmentRepository appointmentRepository;
    private final AppointmentTypeRepository appointmentTypeRepository;

    @Override
    public List<AvailabilitySlotResponse> getAvailability(UUID doctorId,
                                                          UUID appointmentTypeId,
                                                          LocalDate date) {

        String dayOfWeek = date.getDayOfWeek().name();

        // 1. Obtener duración del tipo de cita
        AppointmentType appointmentType = appointmentTypeRepository.findById(appointmentTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Tipo de cita no encontrado"));

        int durationMinutes = appointmentType.getDuration();

        // 2. Obtener horarios del doctor
        List<DoctorSchedule> schedules =
                doctorScheduleRepository.findByDoctorIdAndDayOfWeek(doctorId, dayOfWeek);

        if (schedules.isEmpty()) {
            return List.of();
        }

        // 3. Obtener citas del día
        List<Appointment> appointments =
                appointmentRepository.findAppointmentsByDoctorAndDate(doctorId, date);

        List<Appointment> activeAppointments = appointments.stream()
                .filter(a -> a.getStatus() == AppointmentStatus.SCHEDULED
                        || a.getStatus() == AppointmentStatus.CONFIRMED)
                .toList();

        List<AvailabilitySlotResponse> availableSlots = new ArrayList<>();

        // 4. Generar slots dinámicos
        for (DoctorSchedule schedule : schedules) {

            LocalDateTime start = LocalDateTime.of(date, schedule.getStartTime());
            LocalDateTime end = LocalDateTime.of(date, schedule.getEndTime());

            while (!start.plusMinutes(durationMinutes).isAfter(end)) {

                // Copias finales para usar en la lambda
                final LocalDateTime currentStart = start;
                final LocalDateTime currentSlotEnd = start.plusMinutes(durationMinutes);

                boolean isOverlapping = activeAppointments.stream().anyMatch(a ->
                        currentStart.isBefore(a.getEndAt()) && currentSlotEnd.isAfter(a.getStartAt())
                );

                if (!isOverlapping) {
                    availableSlots.add(
                            AvailabilitySlotResponse.builder()
                                    .startAt(currentStart)
                                    .endAt(currentSlotEnd)
                                    .build()
                    );
                }

                start = start.plusMinutes(durationMinutes);
            }
        }

        // Ordenar slots
        availableSlots.sort(Comparator.comparing(AvailabilitySlotResponse::getStartAt));

        return availableSlots;
    }
}