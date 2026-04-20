package com.edu.plataforma_reserva.services;


import com.edu.plataforma_reserva.dtos.AvailabilitySlotResponse;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface AvailabilityService {

    List<AvailabilitySlotResponse> getAvailability(UUID doctorId,
                                                   UUID appointmentTypeId,
                                                   LocalDate date);
}