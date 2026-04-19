package com.edu.plataforma_reserva.repositories;
/*
import com.edu.plataforma_reserva.entities.Doctor;
import com.edu.plataforma_reserva.entities.Specialty;
import com.edu.plataforma_reserva.enums.DoctorStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DoctorRepositoryIT extends AbstractRepositoryIT {

    @Autowired
    private DoctorRepository doctorRepository;

    // Necesitamos guardar specialties para relacionarlas con doctor
    @Autowired
    private SpecialtyRepository specialtyRepository;

    @Test
    void shouldSaveDoctor() {

        // =========================
        // ARRANGE
        // Creamos una especialidad (obligatoria por la relación)
        // =========================
        Specialty specialty = Specialty.builder()
                .name("Medicina General")
                .build();

        Specialty savedSpecialty = specialtyRepository.save(specialty);

        // Creamos el doctor asociado a esa especialidad
        Doctor doctor = Doctor.builder()
                .name("Dr. Juan")
                .specialty(savedSpecialty)
                .status(DoctorStatus.ACTIVO)
                .build();

        // =========================
        // ACT
        // =========================
        Doctor savedDoctor = doctorRepository.save(doctor);

        // =========================
        // ASSERT
        // =========================
        assertThat(savedDoctor.getId()).isNotNull();
    }

    @Test
    void shouldFindDoctorById() {

        // ARRANGE
        Specialty specialty = specialtyRepository.save(
                Specialty.builder().name("Psicología").build()
        );

        Doctor doctor = doctorRepository.save(
                Doctor.builder()
                        .name("Dr. Maria")
                        .specialty(specialty)
                        .status(DoctorStatus.ACTIVO)
                        .build()
        );

        // ACT
        Doctor found = doctorRepository.findById(doctor.getId()).orElse(null);

        // ASSERT
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Dr. Maria");
    }

    @Test
    void shouldFindAllDoctors() {

        // ARRANGE
        Specialty specialty = specialtyRepository.save(
                Specialty.builder().name("Fisioterapia").build()
        );

        doctorRepository.save(
                Doctor.builder()
                        .name("Dr. A")
                        .specialty(specialty)
                        .status(DoctorStatus.ACTIVO)
                        .build()
        );

        doctorRepository.save(
                Doctor.builder()
                        .name("Dr. B")
                        .specialty(specialty)
                        .status(DoctorStatus.ACTIVO)
                        .build()
        );

        // ACT
        List<Doctor> doctors = doctorRepository.findAll();

        // ASSERT
        assertThat(doctors).hasSize(2);
    }

    @Test
    void shouldFindActiveDoctorsBySpecialty() {

        // =========================
        // ARRANGE
        // Creamos dos especialidades
        // =========================
        Specialty cardio = specialtyRepository.save(
                Specialty.builder().name("Cardiología").build()
        );

        Specialty neuro = specialtyRepository.save(
                Specialty.builder().name("Neurología").build()
        );

        // Doctores en cardiología
        doctorRepository.save(
                Doctor.builder()
                        .name("Dr. Activo 1")
                        .specialty(cardio)
                        .status(DoctorStatus.ACTIVO)
                        .build()
        );

        doctorRepository.save(
                Doctor.builder()
                        .name("Dr. Inactivo")
                        .specialty(cardio)
                        .status(DoctorStatus.INACTIVO)
                        .build()
        );

        // Doctor en otra especialidad
        doctorRepository.save(
                Doctor.builder()
                        .name("Dr. Otro")
                        .specialty(neuro)
                        .status(DoctorStatus.ACTIVO)
                        .build()
        );

        // =========================
        // ACT
        // Buscamos SOLO activos de cardiología
        // =========================
        List<Doctor> result = doctorRepository
                .findBySpecialtyIdAndStatus(cardio.getId(), DoctorStatus.ACTIVO);

        // =========================
        // ASSERT
        // =========================

        // Debe traer solo 1 (activo de cardiología)
        assertThat(result).hasSize(1);

        // Validamos que sea el correcto
        assertThat(result.get(0).getName()).isEqualTo("Dr. Activo 1");

        // Validamos que todos sean ACTIVE (extra importante)
        assertThat(result)
                .allMatch(d -> d.getStatus() == DoctorStatus.ACTIVO);
    }
}

 */