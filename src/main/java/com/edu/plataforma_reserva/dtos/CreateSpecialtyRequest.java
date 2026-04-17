package com.edu.plataforma_reserva.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSpecialtyRequest {

    @NotBlank(message = "El nombre de la especialidad es obligatoria")
    private String name;
}
