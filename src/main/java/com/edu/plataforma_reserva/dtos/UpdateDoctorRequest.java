package com.edu.plataforma_reserva.dtos;

import com.edu.plataforma_reserva.entities.Specialty;
import com.edu.plataforma_reserva.enums.DoctorStatus;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDoctorRequest {

    private String name;

    private UUID specialtyId;

    private DoctorStatus status;
}
