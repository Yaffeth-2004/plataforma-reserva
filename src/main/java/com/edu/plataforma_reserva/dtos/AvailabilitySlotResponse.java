package com.edu.plataforma_reserva.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AvailabilitySlotResponse {

    private LocalDateTime startAt;
    private LocalDateTime endAt;

}