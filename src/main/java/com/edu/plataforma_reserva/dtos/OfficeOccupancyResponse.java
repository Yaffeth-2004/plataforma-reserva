package com.edu.plataforma_reserva.dtos;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OfficeOccupancyResponse {

    private UUID officeId;
    private String officeName;
    private Long totalAppointments;
}