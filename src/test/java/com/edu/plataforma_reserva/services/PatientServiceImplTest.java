package com.edu.plataforma_reserva.services;

import static org.junit.jupiter.api.Assertions.*;


import com.edu.plataforma_reserva.dtos.CreatePatientRequest;
import com.edu.plataforma_reserva.dtos.PatientResponse;
import com.edu.plataforma_reserva.dtos.UpdatePatientRequest;
import com.edu.plataforma_reserva.entities.Patient;
import com.edu.plataforma_reserva.enums.PatientStatus;
import com.edu.plataforma_reserva.exeptions.BusinessException;
import com.edu.plataforma_reserva.exeptions.ResourceNotFoundException;
import com.edu.plataforma_reserva.mappers.PatientMapper;
import com.edu.plataforma_reserva.repositories.PatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientMapper patientMapper;

    @InjectMocks
    private PatientServiceImpl patientService;

    // 🔹 CREATE
    @Test
    void shouldCreatePatientSuccessfully() {

        CreatePatientRequest request = CreatePatientRequest.builder()
                .name("Juan Perez")
                .identity("123456")
                .phone("3001234567")
                .build();

        Patient patient = Patient.builder()
                .name("Juan Perez")
                .identity("123456")
                .phone("3001234567")
                .build();

        Patient saved = Patient.builder()
                .id(UUID.randomUUID())
                .name("Juan Perez")
                .identity("123456")
                .phone("3001234567")
                .status(PatientStatus.ACTIVO)
                .build();

        PatientResponse response = PatientResponse.builder()
                .id(saved.getId())
                .name(saved.getName())
                .identity(saved.getIdentity())
                .phone(saved.getPhone())
                .status(saved.getStatus())
                .build();

        when(patientMapper.toEntity(request)).thenReturn(patient);
        when(patientRepository.save(any(Patient.class))).thenReturn(saved);
        when(patientMapper.toResponse(saved)).thenReturn(response);

        PatientResponse result = patientService.createPatient(request);

        assertNotNull(result);
        assertEquals("Juan Perez", result.getName());
        assertEquals("123456", result.getIdentity());
        assertEquals(PatientStatus.ACTIVO, result.getStatus());

        verify(patientRepository).save(any(Patient.class));
    }

    // 🔹 GET BY ID
    @Test
    void shouldReturnPatientById() {

        UUID id = UUID.randomUUID();

        Patient patient = Patient.builder()
                .id(id)
                .name("Juan")
                .identity("123")
                .phone("300")
                .status(PatientStatus.ACTIVO)
                .build();

        PatientResponse response = PatientResponse.builder()
                .id(id)
                .name("Juan")
                .identity("123")
                .phone("300")
                .status(PatientStatus.ACTIVO)
                .build();

        when(patientRepository.findById(id)).thenReturn(Optional.of(patient));
        when(patientMapper.toResponse(patient)).thenReturn(response);

        PatientResponse result = patientService.getPatientById(id);

        assertEquals(id, result.getId());
        assertEquals("Juan", result.getName());
    }

    // 🔹 NOT FOUND
    @Test
    void shouldThrowWhenPatientNotFound() {

        UUID id = UUID.randomUUID();

        when(patientRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> patientService.getPatientById(id));
    }

    // 🔹 GET ALL
    @Test
    void shouldReturnAllPatients() {

        Patient patient = Patient.builder()
                .id(UUID.randomUUID())
                .name("Juan")
                .identity("123")
                .phone("300")
                .status(PatientStatus.ACTIVO)
                .build();

        PatientResponse response = PatientResponse.builder()
                .id(patient.getId())
                .name(patient.getName())
                .identity(patient.getIdentity())
                .phone(patient.getPhone())
                .status(patient.getStatus())
                .build();

        when(patientRepository.findAll()).thenReturn(List.of(patient));
        when(patientMapper.toResponse(patient)).thenReturn(response);

        List<PatientResponse> result = patientService.getAllPatients();

        assertEquals(1, result.size());
    }

    // 🔹 UPDATE
    @Test
    void shouldUpdatePatientSuccessfully() {

        UUID id = UUID.randomUUID();

        UpdatePatientRequest request = UpdatePatientRequest.builder()
                .name("Nuevo Nombre")
                .phone("999999")
                .status(PatientStatus.ACTIVO)
                .build();

        Patient patient = Patient.builder()
                .id(id)
                .name("Viejo")
                .identity("123")
                .phone("300")
                .status(PatientStatus.ACTIVO)
                .build();

        Patient updated = Patient.builder()
                .id(id)
                .name("Nuevo Nombre")
                .identity("123")
                .phone("999999")
                .status(PatientStatus.ACTIVO)
                .build();

        PatientResponse response = PatientResponse.builder()
                .id(id)
                .name("Nuevo Nombre")
                .identity("123")
                .phone("999999")
                .status(PatientStatus.ACTIVO)
                .build();

        when(patientRepository.findById(id)).thenReturn(Optional.of(patient));
        when(patientRepository.save(patient)).thenReturn(updated);
        when(patientMapper.toResponse(updated)).thenReturn(response);

        PatientResponse result = patientService.updatePatient(id, request);

        assertEquals("Nuevo Nombre", result.getName());

        verify(patientMapper).updateEntityFromRequest( request,patient);
    }

    // 🔹 UPDATE NOT FOUND
    @Test
    void shouldThrowWhenUpdatingNonExistingPatient() {

        UUID id = UUID.randomUUID();

        when(patientRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> patientService.updatePatient(id, new UpdatePatientRequest()));
    }

    // 🔹 DEACTIVATE
    @Test
    void shouldDeactivatePatient() {

        UUID id = UUID.randomUUID();

        Patient patient = Patient.builder()
                .id(id)
                .status(PatientStatus.ACTIVO)
                .build();

        when(patientRepository.findById(id)).thenReturn(Optional.of(patient));

        patientService.deactivatePatient(id);

        assertEquals(PatientStatus.INACTIVO, patient.getStatus());
        verify(patientRepository).save(patient);
    }

    // 🔹 ALREADY INACTIVE
    @Test
    void shouldThrowWhenPatientAlreadyInactive() {

        UUID id = UUID.randomUUID();

        Patient patient = Patient.builder()
                .id(id)
                .status(PatientStatus.INACTIVO)
                .build();

        when(patientRepository.findById(id)).thenReturn(Optional.of(patient));

        assertThrows(BusinessException.class,
                () -> patientService.deactivatePatient(id));
    }

    // 🔹 DEACTIVATE NOT FOUND
    @Test
    void shouldThrowWhenDeactivatingNonExistingPatient() {

        UUID id = UUID.randomUUID();

        when(patientRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> patientService.deactivatePatient(id));
    }
}