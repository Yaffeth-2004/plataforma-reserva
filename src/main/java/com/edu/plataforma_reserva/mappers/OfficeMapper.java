package com.edu.plataforma_reserva.mappers;

import com.edu.plataforma_reserva.dtos.CreateOfficeRequest;
import com.edu.plataforma_reserva.dtos.UpdateOfficeRequest;
import com.edu.plataforma_reserva.dtos.OfficeResponse;
import com.edu.plataforma_reserva.dtos.CreateOfficeRequest;
import com.edu.plataforma_reserva.entities.Office;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface OfficeMapper {

    // 🔹 Create → Entity
    Office toEntity(CreateOfficeRequest request);

    // 🔹 Entity → Response
    OfficeResponse toResponse(Office office);

    // 🔹 Update parcial
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(UpdateOfficeRequest request, @MappingTarget Office office);
}
