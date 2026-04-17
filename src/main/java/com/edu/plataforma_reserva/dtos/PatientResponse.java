package com.edu.plataforma_reserva.dtos;

import com.edu.plataforma_reserva.enums.PatientStatus;

import lombok.*;

import java.util.UUID;


@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PatientResponse {

    private UUID id;

    private String name;

    private String identity;

    private String phone;

    private PatientStatus status;
}
