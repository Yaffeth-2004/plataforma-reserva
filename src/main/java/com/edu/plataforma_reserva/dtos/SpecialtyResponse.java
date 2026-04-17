package com.edu.plataforma_reserva.dtos;

import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpecialtyResponse {

    private UUID id;
    private String name;
}
