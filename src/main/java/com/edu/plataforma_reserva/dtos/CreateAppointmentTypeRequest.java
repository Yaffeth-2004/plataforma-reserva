package com.edu.plataforma_reserva.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAppointmentTypeRequest {

    @NotBlank(message = "El tipo de cita es obligatorio")
    private String name;
    @NotBlank(message = "La duracion del tipo de cita es obligatorio")
    @Positive(message = "La duracion del tipo de cita debe ser mayor a 0")
    private Integer duration;
}
