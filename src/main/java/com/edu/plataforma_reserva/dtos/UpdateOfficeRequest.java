package com.edu.plataforma_reserva.dtos;

import com.edu.plataforma_reserva.enums.OfficeStatus;
import com.edu.plataforma_reserva.enums.OfficeStatus;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateOfficeRequest {

    private String name;
    private String location;
    private OfficeStatus status;
}
