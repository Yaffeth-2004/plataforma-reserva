package com.edu.plataforma_reserva.dtos;

import com.edu.plataforma_reserva.enums.PatientStatus;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePatientRequest {

    private String name;

    private String phone;

    private PatientStatus status;
}
