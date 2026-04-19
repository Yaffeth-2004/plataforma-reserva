package com.edu.plataforma_reserva.repositories;

/*
import com.edu.plataforma_reserva.entities.Doctor;
import com.edu.plataforma_reserva.entities.DoctorSchedule;
import com.edu.plataforma_reserva.entities.Specialty;
import com.edu.plataforma_reserva.enums.DoctorStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class DoctorScheduleRepositoryIT extends AbstractRepositoryIT {

    @Autowired
    private DoctorScheduleRepository doctorScheduleRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private SpecialtyRepository specialtyRepository;

    @Test
    void shouldSaveDoctorSchedule() {

        // =========================
        // ARRANGE
        // Creamos especialidad y doctor (dependencias obligatorias)
        // =========================
        Specialty specialty = specialtyRepository.save(
                Specialty.builder().name("General").build()
        );

        Doctor doctor = doctorRepository.save(
                Doctor.builder()
                        .name("Dr. Juan")
                        .specialty(specialty)
                        .status(DoctorStatus.ACTIVO)
                        .build()
        );

        DoctorSchedule schedule = DoctorSchedule.builder()
                .dayOfWeek("MONDAY")
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(12, 0))
                .doctor(doctor)
                .build();

        // =========================
        // ACT
        // =========================
        DoctorSchedule saved = doctorScheduleRepository.save(schedule);

        // =========================
        // ASSERT
        // =========================
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void shouldFindAllSchedules() {

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

        doctorScheduleRepository.save(
                DoctorSchedule.builder()
                        .dayOfWeek("MONDAY")
                        .startTime(LocalTime.of(8, 0))
                        .endTime(LocalTime.of(10, 0))
                        .doctor(doctor)
                        .build()
        );

        doctorScheduleRepository.save(
                DoctorSchedule.builder()
                        .dayOfWeek("TUESDAY")
                        .startTime(LocalTime.of(10, 0))
                        .endTime(LocalTime.of(12, 0))
                        .doctor(doctor)
                        .build()
        );

        // ACT
        List<DoctorSchedule> schedules = doctorScheduleRepository.findAll();

        // ASSERT
        assertThat(schedules).hasSize(2);
    }

    @Test
    void shouldFindSchedulesByDoctorAndDay() {

        // =========================
        // ARRANGE
        // =========================

        Specialty specialty = specialtyRepository.save(
                Specialty.builder().name("Fisioterapia").build()
        );

        Doctor doctor = doctorRepository.save(
                Doctor.builder()
                        .name("Dr. Carlos")
                        .specialty(specialty)
                        .status(DoctorStatus.ACTIVO)
                        .build()
        );

        // Horario lunes (el que queremos encontrar)
        doctorScheduleRepository.save(
                DoctorSchedule.builder()
                        .dayOfWeek("MONDAY")
                        .startTime(LocalTime.of(8, 0))
                        .endTime(LocalTime.of(12, 0))
                        .doctor(doctor)
                        .build()
        );

        // Horario martes (NO debe aparecer)
        doctorScheduleRepository.save(
                DoctorSchedule.builder()
                        .dayOfWeek("TUESDAY")
                        .startTime(LocalTime.of(14, 0))
                        .endTime(LocalTime.of(18, 0))
                        .doctor(doctor)
                        .build()
        );

        // =========================
        // ACT
        // Buscamos SOLO lunes
        // =========================
        List<DoctorSchedule> result =
                doctorScheduleRepository.findByDoctorIdAndDayOfWeek(
                        doctor.getId(),
                        "MONDAY"
                );

        // =========================
        // ASSERT
        // =========================

        // Debe traer solo 1 horario
        assertThat(result).hasSize(1);

        // Validamos que sea lunes
        assertThat(result.get(0).getDayOfWeek()).isEqualTo("MONDAY");

        // Validamos que pertenece al doctor correcto
        assertThat(result.get(0).getDoctor().getId()).isEqualTo(doctor.getId());
    }
}

 */