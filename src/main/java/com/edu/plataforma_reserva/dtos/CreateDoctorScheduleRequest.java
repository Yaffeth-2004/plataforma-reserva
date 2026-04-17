package com.edu.plataforma_reserva.dtos;

import com.edu.plataforma_reserva.entities.Doctor;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDoctorScheduleRequest {


    @NotBlank(message = "El dia de la semana del horario del doctor es obligatorio")
    private String dayOfWeek;

    @NotNull(message = "El inicio del horario del doctor es obligatorio")
    private LocalTime startTime;
    @NotNull(message = "El fin del horario del doctor es obligatorio")
    private LocalTime endTime;

   @NotNull(message = "El ID del doctor en este horario es obligatorio")
    private UUID doctorId;
}