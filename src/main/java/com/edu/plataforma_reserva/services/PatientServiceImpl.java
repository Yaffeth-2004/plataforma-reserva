package com.edu.plataforma_reserva.services;

import com.edu.plataforma_reserva.dtos.CreatePatientRequest;
import com.edu.plataforma_reserva.dtos.PatientResponse;
import com.edu.plataforma_reserva.dtos.UpdatePatientRequest;
import com.edu.plataforma_reserva.entities.Patient;
import com.edu.plataforma_reserva.enums.PatientStatus;
import com.edu.plataforma_reserva.exeptions.BusinessException;
import com.edu.plataforma_reserva.exeptions.ResourceNotFoundException;
import com.edu.plataforma_reserva.mappers.PatientMapper;
import com.edu.plataforma_reserva.repositories.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {

    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;

    // 🔹 Crear paciente
    @Override
    public PatientResponse createPatient(CreatePatientRequest request) {

        Patient patient = patientMapper.toEntity(request);

        // Estado inicial obligatorio
        patient.setStatus(PatientStatus.ACTIVO);

        Patient saved = patientRepository.save(patient);

        return patientMapper.toResponse(saved);
    }

    // 🔹 Obtener paciente por ID
    @Override
    public PatientResponse getPatientById(UUID id) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado"));

        return patientMapper.toResponse(patient);
    }

    // 🔹 Listar todos los pacientes
    @Override
    public List<PatientResponse> getAllPatients() {

        return patientRepository.findAll()
                .stream()
                .map(patientMapper::toResponse)
                .toList();
    }

    // 🔹 Actualizar paciente
    @Override
    public PatientResponse updatePatient(UUID id, UpdatePatientRequest request) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado"));

        // Actualiza solo los campos necesarios (NO crear uno nuevo)
        patientMapper.updateEntityFromRequest( request,patient);

        Patient updated = patientRepository.save(patient);

        return patientMapper.toResponse(updated);
    }

    // 🔹 Desactivar paciente (soft delete)
    @Override
    public void deactivatePatient(UUID id) {

        Patient patient = patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado"));

        if (patient.getStatus() == PatientStatus.INACTIVO) {
            throw new BusinessException("El paciente ya está inactivo");
        }

        patient.setStatus(PatientStatus.INACTIVO);

        patientRepository.save(patient);
    }
}