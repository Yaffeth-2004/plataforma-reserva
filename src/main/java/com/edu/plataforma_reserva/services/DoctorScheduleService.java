package com.edu.plataforma_reserva.services;

import com.edu.plataforma_reserva.dtos.CreateDoctorScheduleRequest;
import com.edu.plataforma_reserva.dtos.DoctorScheduleResponse;

import java.util.List;
import java.util.UUID;

public interface DoctorScheduleService {


    DoctorScheduleResponse createSchedule(UUID doctorId, CreateDoctorScheduleRequest request);
    List<DoctorScheduleResponse> getSchedulesByDoctor(UUID doctorId);
}
