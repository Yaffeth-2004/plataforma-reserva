package com.edu.plataforma_reserva.services;

import com.edu.plataforma_reserva.dtos.CreateDoctorScheduleRequest;
import com.edu.plataforma_reserva.dtos.DoctorScheduleResponse;
import com.edu.plataforma_reserva.entities.Doctor;
import com.edu.plataforma_reserva.entities.DoctorSchedule;
import com.edu.plataforma_reserva.exeptions.BusinessException;
import com.edu.plataforma_reserva.exeptions.ResourceNotFoundException;
import com.edu.plataforma_reserva.mappers.DoctorScheduleMapper;
import com.edu.plataforma_reserva.repositories.DoctorRepository;
import com.edu.plataforma_reserva.repositories.DoctorScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DoctorScheduleServiceImpl implements DoctorScheduleService {

    private final DoctorScheduleRepository scheduleRepository;
    private final DoctorRepository doctorRepository;
    private final DoctorScheduleMapper scheduleMapper;

    // 🔹 CREATE SCHEDULE
    @Override
    public DoctorScheduleResponse createSchedule(UUID doctorId, CreateDoctorScheduleRequest request) {

        // 🔥 Validar doctor
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor no encontrado"));

        // 🔥 Validar horas
        if (request.getStartTime().isAfter(request.getEndTime())
                || request.getStartTime().equals(request.getEndTime())) {
            throw new BusinessException("La hora de inicio debe ser menor a la hora de fin");
        }

        // 🔥 Validar traslape
        boolean overlap = scheduleRepository.existsOverlappingSchedule(
                doctorId,
                request.getDayOfWeek(),
                request.getStartTime(),
                request.getEndTime()
        );

        if (overlap) {
            throw new BusinessException("Ya existe un horario que se traslapa");
        }

        // 🔥 Mapear y asignar doctor
        DoctorSchedule schedule = scheduleMapper.toEntity(request);
        schedule.setDoctor(doctor);

        DoctorSchedule saved = scheduleRepository.save(schedule);

        return scheduleMapper.toResponse(saved);
    }

    // 🔹 GET BY DOCTOR
    @Override
    public List<DoctorScheduleResponse> getSchedulesByDoctor(UUID doctorId) {

        return scheduleRepository.findByDoctorId(doctorId)
                .stream()
                .map(scheduleMapper::toResponse)
                .toList();
    }
}