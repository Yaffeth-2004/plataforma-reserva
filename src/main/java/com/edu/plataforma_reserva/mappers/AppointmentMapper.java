package com.edu.plataforma_reserva.mappers;

import com.edu.plataforma_reserva.dtos.CreateAppointmentRequest;
import com.edu.plataforma_reserva.dtos.AppointmentResponse;
import com.edu.plataforma_reserva.entities.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface AppointmentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "endAt", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "observations", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)

    @Mapping(target = "patient", source = "patientId", qualifiedByName = "toPatient")
    @Mapping(target = "doctor", source = "doctorId", qualifiedByName = "toDoctor")
    @Mapping(target = "office", source = "officeId", qualifiedByName = "toOffice")
    @Mapping(target = "appointmentType", source = "appointmentTypeId", qualifiedByName = "toAppointmentType")
    Appointment toEntity(CreateAppointmentRequest request);

    @Mapping(target = "patientId", source = "patient.id")
    @Mapping(target = "patientName", source = "patient.name")
    @Mapping(target = "doctorId", source = "doctor.id")
    @Mapping(target = "doctorName", source = "doctor.name")
    @Mapping(target = "officeId", source = "office.id")
    @Mapping(target = "officeName", source = "office.name")
    @Mapping(target = "appointmentTypeId", source = "appointmentType.id")
    @Mapping(target = "appointmentTypeName", source = "appointmentType.name")
    AppointmentResponse toResponse(Appointment appointment);

    @Named("toPatient")
    default Patient mapPatient(UUID id) {
        return id != null ? Patient.builder().id(id).build() : null;
    }

    @Named("toDoctor")
    default Doctor mapDoctor(UUID id) {
        return id != null ? Doctor.builder().id(id).build() : null;
    }

    @Named("toOffice")
    default Office mapOffice(UUID id) {
        return id != null ? Office.builder().id(id).build() : null;
    }

    @Named("toAppointmentType")
    default AppointmentType mapAppointmentType(UUID id) {
        return id != null ? AppointmentType.builder().id(id).build() : null;
    }
}