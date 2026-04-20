package com.edu.plataforma_reserva.dtos;

import com.edu.plataforma_reserva.entities.Specialty;
import com.edu.plataforma_reserva.enums.DoctorStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDoctorRequest {

    @NotBlank(message = "El nombre del doctor es obligatorio")
    private String name;

    @NotNull(message = "El ID de la especialidad del doctor es obligatoria")
    private UUID specialtyId;


}