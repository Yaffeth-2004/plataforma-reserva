package com.edu.plataforma_reserva.dtos;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorProductivityResponse {

    private UUID doctorId;
    private String doctorName;
    private Long completedAppointments;
}
