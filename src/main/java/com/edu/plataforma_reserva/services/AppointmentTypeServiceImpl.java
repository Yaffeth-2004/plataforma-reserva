package com.edu.plataforma_reserva.services;


import com.edu.plataforma_reserva.dtos.CreateAppointmentTypeRequest;
import com.edu.plataforma_reserva.dtos.AppointmentTypeResponse;
import com.edu.plataforma_reserva.entities.AppointmentType;
import com.edu.plataforma_reserva.exeptions.BusinessException;
import com.edu.plataforma_reserva.mappers.AppointmentTypeMapper;
import com.edu.plataforma_reserva.repositories.AppointmentTypeRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentTypeServiceImpl implements AppointmentTypeService {

    private final AppointmentTypeRepository appointmentTypeRepository;
    private final AppointmentTypeMapper appointmentTypeMapper;

    // 🔹 CREATE
    @Override
    public AppointmentTypeResponse createAppointmentType(CreateAppointmentTypeRequest request) {

        // 🔥 Validación de duración (extra a @Positive, por seguridad backend)
        if (request.getDuration() == null || request.getDuration() <= 0) {
            throw new BusinessException("La duración debe ser mayor a 0");
        }

        // 🔥 Validar duplicados
        if (appointmentTypeRepository.existsByNameIgnoreCase(request.getName())) {
            throw new BusinessException("El tipo de cita ya existe");
        }

        AppointmentType appointmentType = appointmentTypeMapper.toEntity(request);

        AppointmentType saved = appointmentTypeRepository.save(appointmentType);

        return appointmentTypeMapper.toResponse(saved);
    }

    // 🔹 GET ALL
    @Override
    public List<AppointmentTypeResponse> getAllAppointmentTypes() {

        return appointmentTypeRepository.findAll()
                .stream()
                .map(appointmentTypeMapper::toResponse)
                .toList();
    }
}
