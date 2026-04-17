package com.edu.plataforma_reserva.dtos;

import com.edu.plataforma_reserva.entities.Specialty;
import com.edu.plataforma_reserva.enums.DoctorStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorResponse {

    private UUID id;

    private String name;

    private UUID specialtyId;
    private String specialtyName;

    private DoctorStatus status;
}
