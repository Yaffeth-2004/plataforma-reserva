package com.edu.plataforma_reserva.services;

import com.edu.plataforma_reserva.dtos.AppointmentResponse;
import com.edu.plataforma_reserva.dtos.CancelAppointmentRequest;
import com.edu.plataforma_reserva.dtos.CreateAppointmentRequest;

import java.util.List;
import java.util.UUID;

public interface AppointmentService {

    AppointmentResponse create(CreateAppointmentRequest request);

    AppointmentResponse findById(UUID id);

    List<AppointmentResponse> findAll();

    AppointmentResponse confirm(UUID id);

    AppointmentResponse cancel(UUID id, CancelAppointmentRequest request);

    AppointmentResponse complete(UUID id, String observations);

    AppointmentResponse noShow(UUID id);
}