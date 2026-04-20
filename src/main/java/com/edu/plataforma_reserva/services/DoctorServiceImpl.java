package com.edu.plataforma_reserva.services;


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
import com.edu.plataforma_reserva.services.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorRepository doctorRepository;
    private final SpecialtyRepository specialtyRepository;
    private final DoctorMapper doctorMapper;

    // 🔹 CREATE
    @Override
    public DoctorResponse createDoctor(CreateDoctorRequest request) {

        // Validar especialidad
        Specialty specialty = specialtyRepository.findById(request.getSpecialtyId())
                .orElseThrow(() -> new ResourceNotFoundException("Especialidad no encontrada"));

        Doctor doctor = doctorMapper.toEntity(request);

        doctor.setSpecialty(specialty);
        doctor.setStatus(DoctorStatus.ACTIVO);

        Doctor saved = doctorRepository.save(doctor);

        return doctorMapper.toResponse(saved);
    }

    // 🔹 GET BY ID
    @Override
    public DoctorResponse getDoctorById(UUID id) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor no encontrado"));

        return doctorMapper.toResponse(doctor);
    }

    // 🔹 GET ALL
    @Override
    public List<DoctorResponse> getAllDoctors() {

        return doctorRepository.findAll()
                .stream()
                .map(doctorMapper::toResponse)
                .toList();
    }

    // 🔹 UPDATE
    @Override
    public DoctorResponse updateDoctor(UUID id, UpdateDoctorRequest request) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor no encontrado"));

        // Si cambia especialidad
        if (request.getSpecialtyId() != null) {
            Specialty specialty = specialtyRepository.findById(request.getSpecialtyId())
                    .orElseThrow(() -> new ResourceNotFoundException("Especialidad no encontrada"));
            doctor.setSpecialty(specialty);
        }

        doctorMapper.updateEntityFromRequest( request,doctor);

        Doctor updated = doctorRepository.save(doctor);

        return doctorMapper.toResponse(updated);
    }

    // 🔹 GET BY SPECIALTY (solo activos)
    @Override
    public List<DoctorResponse> getDoctorsBySpecialty(UUID specialtyId) {

        return doctorRepository
                .findBySpecialtyIdAndStatus(specialtyId, DoctorStatus.ACTIVO)
                .stream()
                .map(doctorMapper::toResponse)
                .toList();
    }

    // 🔹 DEACTIVATE
    @Override
    public void deactivateDoctor(UUID id) {

        Doctor doctor = doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor no encontrado"));

        if (doctor.getStatus() == DoctorStatus.INACTIVO) {
            throw new BusinessException("El doctor ya está inactivo");
        }

        doctor.setStatus(DoctorStatus.INACTIVO);

        doctorRepository.save(doctor);
    }
}