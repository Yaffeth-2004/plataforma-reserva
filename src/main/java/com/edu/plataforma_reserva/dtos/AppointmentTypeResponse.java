package com.edu.plataforma_reserva.dtos;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentTypeResponse {

    private UUID id;
    private String name;
    private Integer duration;
}