package com.edu.plataforma_reserva.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateAppointmentRequest {

    @NotNull(message = "El ID del paciente es obligatorio")
    private UUID patientId;

    @NotNull(message = "El ID del doctor es obligatorio")
    private UUID doctorId;

    @NotNull(message = "El ID del consultorio es obligatorio")
    private UUID officeId;

    @NotNull(message = "El ID del tipo de cita es obligatorio")
    private UUID appointmentTypeId;

    @NotNull(message = "La fecha y hora de inicio es obligatoria")
    private LocalDateTime startAt;
}