package com.edu.plataforma_reserva.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancelAppointmentRequest {
    @NotBlank(message = "El motivo de cancelacion de cita es obligatorio")
    private String reason;
}
