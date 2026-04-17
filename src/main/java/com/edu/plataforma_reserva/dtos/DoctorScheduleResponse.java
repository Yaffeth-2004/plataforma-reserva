package com.edu.plataforma_reserva.dtos;

import lombok.*;

import java.time.LocalTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DoctorScheduleResponse {

    private UUID id;
    private String dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
    private UUID doctorId;
    private String doctorName;
}
