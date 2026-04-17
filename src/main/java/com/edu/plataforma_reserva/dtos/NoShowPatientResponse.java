package com.edu.plataforma_reserva.dtos;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class NoShowPatientResponse {

    private UUID patientId;
    private String patientName;

    private Long totalNoShows;


}