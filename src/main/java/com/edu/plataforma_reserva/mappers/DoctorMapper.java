package com.edu.plataforma_reserva.mappers;

import com.edu.plataforma_reserva.dtos.CreateDoctorRequest;
import com.edu.plataforma_reserva.dtos.UpdateDoctorRequest;
import com.edu.plataforma_reserva.dtos.DoctorResponse;
import com.edu.plataforma_reserva.entities.Doctor;
import com.edu.plataforma_reserva.entities.Specialty;
import org.mapstruct.*;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface DoctorMapper {

    // 🔹 Create → Entity
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "specialty", source = "specialtyId", qualifiedByName = "toSpecialty")
    Doctor toEntity(CreateDoctorRequest request);

    // 🔹 Entity → Response
    @Mapping(target = "specialtyId", source = "specialty.id")
    @Mapping(target = "specialtyName", source = "specialty.name")
    DoctorResponse toResponse(Doctor doctor);

    // 🔹 Update parcial
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "specialty", source = "specialtyId", qualifiedByName = "toSpecialty")
    void updateEntityFromRequest(UpdateDoctorRequest request, @MappingTarget Doctor doctor);

    // 🔥 Método con @Named para convertir UUID → Specialty
    @Named("toSpecialty")
    default Specialty mapToSpecialty(UUID specialtyId) {
        if (specialtyId == null) {
            return null;
        }
        return Specialty.builder()
                .id(specialtyId)
                .build();
    }
}