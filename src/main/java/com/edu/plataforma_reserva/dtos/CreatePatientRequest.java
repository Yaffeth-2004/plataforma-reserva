package com.edu.plataforma_reserva.dtos;

import com.edu.plataforma_reserva.enums.PatientStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePatientRequest {

    @NotBlank(message = "EL nombre del paciente es obligatorio")
    private String name;
    @NotBlank(message = "El documento es obligatorio")
    private String identity;
     @NotBlank(message = "El contacto es obligatorio")
    private String phone;
     @NotNull(message = "EL estado  del paciente es obligatorio")
    private PatientStatus status;
}
