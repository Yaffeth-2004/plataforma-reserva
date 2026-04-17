package com.edu.plataforma_reserva.mappers;

import com.edu.plataforma_reserva.dtos.CreateAppointmentTypeRequest;

import com.edu.plataforma_reserva.entities.AppointmentType;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AppointmentTypeMapper {

    // 🔹 Create → Entity

    AppointmentType toEntity(CreateAppointmentTypeRequest request);

    // 🔹 Entity → Response
    AppointmentType toResponse(AppointmentType appointmentType);
}