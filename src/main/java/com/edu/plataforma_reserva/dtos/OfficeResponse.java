package com.edu.plataforma_reserva.dtos;

import com.edu.plataforma_reserva.enums.OfficeStatus;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OfficeResponse {

    private UUID id;
    private String name;
    private String location;
    private OfficeStatus status;
}
