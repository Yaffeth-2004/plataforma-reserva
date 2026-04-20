package com.edu.plataforma_reserva.services;

import com.edu.plataforma_reserva.dtos.CreateDoctorRequest;
import com.edu.plataforma_reserva.dtos.DoctorResponse;
import com.edu.plataforma_reserva.dtos.UpdateDoctorRequest;

import java.util.List;
import java.util.UUID;

public interface DoctorService {

    DoctorResponse createDoctor(CreateDoctorRequest request);

    DoctorResponse getDoctorById(UUID id);

    List<DoctorResponse> getAllDoctors();

    DoctorResponse updateDoctor(UUID id, UpdateDoctorRequest request);

    List<DoctorResponse> getDoctorsBySpecialty(UUID specialtyId);

    void deactivateDoctor(UUID id);
}