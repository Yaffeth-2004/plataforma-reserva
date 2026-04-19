package com.edu.plataforma_reserva.repositories;


/*
import com.edu.plataforma_reserva.entities.*;
import com.edu.plataforma_reserva.enums.AppointmentStatus;
import com.edu.plataforma_reserva.enums.DoctorStatus;
import com.edu.plataforma_reserva.enums.PatientStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class AppointmentRepositoryIT extends AbstractRepositoryIT {

    @Autowired private AppointmentRepository appointmentRepository;
    @Autowired private PatientRepository patientRepository;
    @Autowired private DoctorRepository doctorRepository;
    @Autowired private SpecialtyRepository specialtyRepository;
    @Autowired private OfficeRepository officeRepository;
    @Autowired private AppointmentTypeRepository appointmentTypeRepository;

    // =========================
    // 🔥 1. TRASLAPE DOCTOR
    // =========================
    @Test
    void shouldDetectDoctorOverlap() {

        // ARRANGE
        Patient patient = patientRepository.save(
                Patient.builder()
                        .name("Paciente")
                        .identity("111")
                        .phone("300")
                        .status(PatientStatus.ACTIVO)
                        .build()
        );

        Specialty specialty = specialtyRepository.save(
                Specialty.builder().name("General").build()
        );

        Doctor doctor = doctorRepository.save(
                Doctor.builder()
                        .name("Dr")
                        .specialty(specialty)
                        .status(DoctorStatus.ACTIVO)
                        .build()
        );

        Office office = officeRepository.save(
                Office.builder().name("101").build()
        );

        AppointmentType type = appointmentTypeRepository.save(
                AppointmentType.builder().name("Consulta").duration(30).build()
        );

        LocalDateTime start = LocalDateTime.of(2026, 1, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2026, 1, 1, 10, 30);

        // Cita existente
        appointmentRepository.save(
                Appointment.builder()
                        .startAt(start)
                        .endAt(end)
                        .status(AppointmentStatus.SCHEDULED)
                        .patient(patient)
                        .doctor(doctor)
                        .office(office)
                        .appointmentType(type)
                        .build()
        );

        // ACT → nueva cita que se cruza
        boolean overlap = appointmentRepository.existsDoctorOverlap(
                doctor.getId(),
                LocalDateTime.of(2026, 1, 1, 10, 15),
                LocalDateTime.of(2026, 1, 1, 10, 45)
        );

        // ASSERT
        assertThat(overlap).isTrue();
    }

    // =========================
    // 🔥 2. TRASLAPE CONSULTORIO
    // =========================
    @Test
    void shouldDetectOfficeOverlap() {

        // (mismo setup simplificado)
        Patient patient = patientRepository.save(
                Patient.builder().name("P").identity("222").phone("300").status(PatientStatus.ACTIVO).build()
        );

        Specialty specialty = specialtyRepository.save(
                Specialty.builder().name("General").build()
        );

        Doctor doctor = doctorRepository.save(
                Doctor.builder().name("Dr").specialty(specialty).status(DoctorStatus.ACTIVO).build()
        );

        Office office = officeRepository.save(
                Office.builder().name("102").build()
        );

        AppointmentType type = appointmentTypeRepository.save(
                AppointmentType.builder().name("Consulta").duration(30).build()
        );

        appointmentRepository.save(
                Appointment.builder()
                        .startAt(LocalDateTime.of(2026, 1, 1, 8, 0))
                        .endAt(LocalDateTime.of(2026, 1, 1, 8, 30))
                        .status(AppointmentStatus.SCHEDULED)
                        .patient(patient)
                        .doctor(doctor)
                        .office(office)
                        .appointmentType(type)
                        .build()
        );

        boolean overlap = appointmentRepository.existsOfficeOverlap(
                office.getId(),
                LocalDateTime.of(2026, 1, 1, 8, 15),
                LocalDateTime.of(2026, 1, 1, 8, 45)
        );

        assertThat(overlap).isTrue();
    }

    // =========================
    // 🔥 3. RANGO DE FECHAS
    // =========================
    @Test
    void shouldFindAppointmentsByDateRange() {

        // ARRANGE
        Patient patient = patientRepository.save(
                Patient.builder().name("P").identity("333").phone("300").status(PatientStatus.ACTIVO).build()
        );

        Specialty specialty = specialtyRepository.save(
                Specialty.builder().name("General").build()
        );

        Doctor doctor = doctorRepository.save(
                Doctor.builder().name("Dr").specialty(specialty).status(DoctorStatus.ACTIVO).build()
        );

        Office office = officeRepository.save(
                Office.builder().name("103").build()
        );

        AppointmentType type = appointmentTypeRepository.save(
                AppointmentType.builder().name("Consulta").duration(30).build()
        );

        appointmentRepository.save(
                Appointment.builder()
                        .startAt(LocalDateTime.of(2026, 1, 1, 9, 0))
                        .endAt(LocalDateTime.of(2026, 1, 1, 9, 30))
                        .status(AppointmentStatus.SCHEDULED)
                        .patient(patient)
                        .doctor(doctor)
                        .office(office)
                        .appointmentType(type)
                        .build()
        );

        // ACT
        List<Appointment> result = appointmentRepository.findByStartAtBetween(
                LocalDateTime.of(2026, 1, 1, 8, 0),
                LocalDateTime.of(2026, 1, 1, 10, 0)
        );

        // ASSERT
        assertThat(result).hasSize(1);
    }

    // =========================
    // 🔥 4. OCUPACIÓN
    // =========================
    @Test
    void shouldCalculateOfficeOccupancy() {

        // (setup reducido)
        Patient p = patientRepository.save(
                Patient.builder().name("P").identity("444").phone("300").status(PatientStatus.ACTIVO).build()
        );

        Specialty s = specialtyRepository.save(Specialty.builder().name("General").build());

        Doctor d = doctorRepository.save(
                Doctor.builder().name("Dr").specialty(s).status(DoctorStatus.ACTIVO).build()
        );

        Office office = officeRepository.save(
                Office.builder().name("104").build()
        );

        AppointmentType t = appointmentTypeRepository.save(
                AppointmentType.builder().name("Consulta").duration(30).build()
        );

        appointmentRepository.save(
                Appointment.builder()
                        .startAt(LocalDateTime.of(2026, 1, 1, 7, 0))
                        .endAt(LocalDateTime.of(2026, 1, 1, 7, 30))
                        .status(AppointmentStatus.SCHEDULED)
                        .patient(p)
                        .doctor(d)
                        .office(office)
                        .appointmentType(t)
                        .build()
        );

        // ACT
        Long count = appointmentRepository.countOfficeOccupancy(
                office.getId(),
                LocalDateTime.of(2026, 1, 1, 6, 0),
                LocalDateTime.of(2026, 1, 1, 8, 0)
        );

        // ASSERT
        assertThat(count).isEqualTo(1);
    }

    // =========================
    // 🔥 5. NO SHOW
    // =========================
    @Test
    void shouldReturnNoShowPatients() {

        Patient p = patientRepository.save(
                Patient.builder().name("P").identity("555").phone("300").status(PatientStatus.ACTIVO).build()
        );

        Specialty s = specialtyRepository.save(Specialty.builder().name("General").build());

        Doctor d = doctorRepository.save(
                Doctor.builder().name("Dr").specialty(s).status(DoctorStatus.ACTIVO).build()
        );

        Office office = officeRepository.save(
                Office.builder().name("105").build()
        );

        AppointmentType t = appointmentTypeRepository.save(
                AppointmentType.builder().name("Consulta").duration(30).build()
        );

        appointmentRepository.save(
                Appointment.builder()
                        .startAt(LocalDateTime.of(2026, 1, 1, 6, 0))
                        .endAt(LocalDateTime.of(2026, 1, 1, 6, 30))
                        .status(AppointmentStatus.NO_SHOW)
                        .patient(p)
                        .doctor(d)
                        .office(office)
                        .appointmentType(t)
                        .build()
        );

        // ACT
        List<Object[]> result = appointmentRepository.getNoShowPatients(
                LocalDateTime.of(2026, 1, 1, 0, 0),
                LocalDateTime.of(2026, 1, 2, 0, 0)
        );

        // ASSERT
        assertThat(result).isNotEmpty();
    }
}

 */