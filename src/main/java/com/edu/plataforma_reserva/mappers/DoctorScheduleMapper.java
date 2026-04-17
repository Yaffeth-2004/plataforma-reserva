package com.edu.plataforma_reserva.mappers;

import com.edu.plataforma_reserva.dtos.CreateDoctorScheduleRequest;
import com.edu.plataforma_reserva.dtos.DoctorScheduleResponse;
import com.edu.plataforma_reserva.entities.Doctor;
import com.edu.plataforma_reserva.entities.DoctorSchedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface DoctorScheduleMapper {

    // 🔹 Create → Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "doctor", source = "doctorId", qualifiedByName = "toDoctor")
    DoctorSchedule toEntity(CreateDoctorScheduleRequest request);

    // 🔹 Entity → Response
    @Mapping(target = "doctorId", source = "doctor.id")
    @Mapping(target = "doctorName", source = "doctor.name")
    DoctorScheduleResponse toResponse(DoctorSchedule schedule);

    // 🔥 Método con @Named para convertir UUID → Doctor
    @Named("toDoctor")
    default Doctor mapToDoctor(UUID doctorId) {
        if (doctorId == null) {
            return null;
        }
        return Doctor.builder()
                .id(doctorId)
                .build();
    }
}