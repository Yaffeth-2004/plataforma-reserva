package com.edu.plataforma_reserva.mappers;



import com.edu.plataforma_reserva.dtos.CreatePatientRequest;
import com.edu.plataforma_reserva.dtos.UpdatePatientRequest;
import com.edu.plataforma_reserva.dtos.PatientResponse;
import com.edu.plataforma_reserva.entities.Patient;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PatientMapper {

    Patient toEntity(CreatePatientRequest request);

    PatientResponse toResponse(Patient patient);


    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE) //NOTA PARA MI: Evitar que se sobreescriba un campo que venga null
    void updateEntityFromRequest(UpdatePatientRequest request, @MappingTarget Patient patient);//NOTA PARA MI: No crees un objeto nuevo actualiza el existente
}
