package com.edu.plataforma_reserva.services;

import static org.junit.jupiter.api.Assertions.*;

import com.edu.plataforma_reserva.dtos.CreateDoctorRequest;
import com.edu.plataforma_reserva.dtos.DoctorResponse;
import com.edu.plataforma_reserva.dtos.UpdateDoctorRequest;
import com.edu.plataforma_reserva.entities.Doctor;
import com.edu.plataforma_reserva.entities.Specialty;
import com.edu.plataforma_reserva.enums.DoctorStatus;
import com.edu.plataforma_reserva.exeptions.BusinessException;
import com.edu.plataforma_reserva.exeptions.ResourceNotFoundException;
import com.edu.plataforma_reserva.mappers.DoctorMapper;
import com.edu.plataforma_reserva.repositories.DoctorRepository;
import com.edu.plataforma_reserva.repositories.SpecialtyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DoctorServiceImplTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private SpecialtyRepository specialtyRepository;

    @Mock
    private DoctorMapper doctorMapper;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    // 🔹 CREATE SUCCESS
    @Test
    void shouldCreateDoctorSuccessfully() {

        UUID specialtyId = UUID.randomUUID();
        UUID doctorId = UUID.randomUUID();

        CreateDoctorRequest request = CreateDoctorRequest.builder()
                .name("Dr. Juan")
                .specialtyId(specialtyId)
                .build();

        Specialty specialty = Specialty.builder()
                .id(specialtyId)
                .name("Medicina General")
                .build();

        Doctor doctor = Doctor.builder()
                .name("Dr. Juan")
                .build();

        Doctor saved = Doctor.builder()
                .id(doctorId)
                .name("Dr. Juan")
                .specialty(specialty)
                .status(DoctorStatus.ACTIVO)
                .build();

        DoctorResponse response = DoctorResponse.builder()
                .id(doctorId)
                .name("Dr. Juan")
                .build();

        when(specialtyRepository.findById(specialtyId)).thenReturn(Optional.of(specialty));
        when(doctorMapper.toEntity(request)).thenReturn(doctor);
        when(doctorRepository.save(doctor)).thenReturn(saved);
        when(doctorMapper.toResponse(saved)).thenReturn(response);

        DoctorResponse result = doctorService.createDoctor(request);

        assertNotNull(result);
        assertEquals("Dr. Juan", result.getName());

        verify(specialtyRepository).findById(specialtyId);
        verify(doctorRepository).save(doctor);
    }

    // 🔹 CREATE - SPECIALTY NOT FOUND
    @Test
    void shouldThrowWhenSpecialtyNotFound() {

        UUID specialtyId = UUID.randomUUID();

        CreateDoctorRequest request = CreateDoctorRequest.builder()
                .name("Dr. Juan")
                .specialtyId(specialtyId)
                .build();

        when(specialtyRepository.findById(specialtyId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> doctorService.createDoctor(request));

        verify(doctorRepository, never()).save(any());
    }

    // 🔹 GET BY ID
    @Test
    void shouldReturnDoctorById() {

        UUID id = UUID.randomUUID();

        Doctor doctor = Doctor.builder()
                .id(id)
                .name("Dr. Juan")
                .build();

        DoctorResponse response = DoctorResponse.builder()
                .id(id)
                .name("Dr. Juan")
                .build();

        when(doctorRepository.findById(id)).thenReturn(Optional.of(doctor));
        when(doctorMapper.toResponse(doctor)).thenReturn(response);

        DoctorResponse result = doctorService.getDoctorById(id);

        assertEquals(id, result.getId());
    }

    // 🔹 GET BY ID NOT FOUND
    @Test
    void shouldThrowWhenDoctorNotFound() {

        UUID id = UUID.randomUUID();

        when(doctorRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> doctorService.getDoctorById(id));
    }

    // 🔹 GET ALL
    @Test
    void shouldReturnAllDoctors() {

        Doctor doctor = Doctor.builder()
                .id(UUID.randomUUID())
                .name("Dr. Juan")
                .build();

        DoctorResponse response = DoctorResponse.builder()
                .id(doctor.getId())
                .name("Dr. Juan")
                .build();

        when(doctorRepository.findAll()).thenReturn(List.of(doctor));
        when(doctorMapper.toResponse(doctor)).thenReturn(response);

        List<DoctorResponse> result = doctorService.getAllDoctors();

        assertEquals(1, result.size());
    }

    // 🔹 UPDATE (cambiando especialidad)
    @Test
    void shouldUpdateDoctorWithNewSpecialty() {

        UUID doctorId = UUID.randomUUID();
        UUID specialtyId = UUID.randomUUID();

        UpdateDoctorRequest request = UpdateDoctorRequest.builder()
                .specialtyId(specialtyId)
                .build();

        Doctor doctor = Doctor.builder()
                .id(doctorId)
                .build();

        Specialty specialty = Specialty.builder()
                .id(specialtyId)
                .build();

        when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(doctor));
        when(specialtyRepository.findById(specialtyId)).thenReturn(Optional.of(specialty));
        when(doctorRepository.save(doctor)).thenReturn(doctor);
        when(doctorMapper.toResponse(doctor)).thenReturn(new DoctorResponse());

        doctorService.updateDoctor(doctorId, request);

        assertEquals(specialty, doctor.getSpecialty());
        verify(doctorMapper).updateEntityFromRequest( request,doctor);
    }

    // 🔹 GET BY SPECIALTY
    @Test
    void shouldReturnDoctorsBySpecialty() {

        UUID specialtyId = UUID.randomUUID();

        Doctor doctor = Doctor.builder()
                .id(UUID.randomUUID())
                .status(DoctorStatus.ACTIVO)
                .build();

        when(doctorRepository.findBySpecialtyIdAndStatus(specialtyId, DoctorStatus.ACTIVO))
                .thenReturn(List.of(doctor));

        when(doctorMapper.toResponse(doctor)).thenReturn(new DoctorResponse());

        List<DoctorResponse> result = doctorService.getDoctorsBySpecialty(specialtyId);

        assertEquals(1, result.size());
    }

    // 🔹 DEACTIVATE
    @Test
    void shouldDeactivateDoctor() {

        UUID id = UUID.randomUUID();

        Doctor doctor = Doctor.builder()
                .id(id)
                .status(DoctorStatus.ACTIVO)
                .build();

        when(doctorRepository.findById(id)).thenReturn(Optional.of(doctor));

        doctorService.deactivateDoctor(id);

        assertEquals(DoctorStatus.INACTIVO, doctor.getStatus());
        verify(doctorRepository).save(doctor);
    }

    // 🔹 DEACTIVATE ALREADY INACTIVE
    @Test
    void shouldThrowWhenDoctorAlreadyInactive() {

        UUID id = UUID.randomUUID();

        Doctor doctor = Doctor.builder()
                .id(id)
                .status(DoctorStatus.INACTIVO)
                .build();

        when(doctorRepository.findById(id)).thenReturn(Optional.of(doctor));

        assertThrows(BusinessException.class,
                () -> doctorService.deactivateDoctor(id));
    }
}