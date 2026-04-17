package com.edu.plataforma_reserva.dtos;

import com.edu.plataforma_reserva.enums.AppointmentStatus;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentResponse {

    private UUID id;
    private LocalDateTime startAt;
    private LocalDateTime endAt;
    private AppointmentStatus status;
    private String observations;

    private UUID patientId;
    private String patientName;

    private UUID doctorId;
    private String doctorName;

    private UUID officeId;
    private String officeName;

    private UUID appointmentTypeId;
    private String appointmentTypeName;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
