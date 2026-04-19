package com.edu.plataforma_reserva.services;

import com.edu.plataforma_reserva.dtos.CreatePatientRequest;
import com.edu.plataforma_reserva.dtos.PatientResponse;
import com.edu.plataforma_reserva.dtos.UpdatePatientRequest;

import java.util.List;
import java.util.UUID;

public interface PatientService {

    PatientResponse createPatient(CreatePatientRequest request);

    PatientResponse getPatientById(UUID id);

    List<PatientResponse> getAllPatients();

    PatientResponse updatePatient(UUID id, UpdatePatientRequest request);

    void deactivatePatient(UUID id);
}

