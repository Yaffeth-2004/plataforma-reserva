package com.edu.plataforma_reserva.mappers;

import com.edu.plataforma_reserva.dtos.CreateSpecialtyRequest;
import com.edu.plataforma_reserva.dtos.SpecialtyResponse;
import com.edu.plataforma_reserva.entities.Specialty;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SpecialtyMapper {

    Specialty toEntity(CreateSpecialtyRequest request);

    SpecialtyResponse toResponse(Specialty specialty);
}
