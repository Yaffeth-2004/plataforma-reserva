package com.edu.plataforma_reserva.dtos;

import com.edu.plataforma_reserva.enums.OfficeStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOfficeRequest {
    @NotBlank(message = "El nombre del consultorio es obligatorio")
    private String name;

    @NotBlank(message = "La ubicacion del consultorio es obligatorio")
    private String location;

    @NotNull(message = "El estado del consultorio es obligatorio")
    private OfficeStatus status;
}
