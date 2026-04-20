package com.edu.plataforma_reserva.services;


import com.edu.plataforma_reserva.dtos.CreateAppointmentTypeRequest;
import com.edu.plataforma_reserva.dtos.AppointmentTypeResponse;

import java.util.List;

public interface AppointmentTypeService {

    AppointmentTypeResponse createAppointmentType(CreateAppointmentTypeRequest request);

    List<AppointmentTypeResponse> getAllAppointmentTypes();
}
