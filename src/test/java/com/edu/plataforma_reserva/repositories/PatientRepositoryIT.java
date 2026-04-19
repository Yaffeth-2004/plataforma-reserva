package com.edu.plataforma_reserva.repositories;


import com.edu.plataforma_reserva.entities.Patient;
import com.edu.plataforma_reserva.enums.PatientStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
/*
class PatientRepositoryIntegrationTest extends AbstractRepositoryIT {

    @Autowired
    private PatientRepository patientRepository;

    @Test
    void shouldSavePatient() {

        // =========================
        // ARRANGE
        // Creamos un paciente sin ID (la BD lo genera)
        // =========================
        Patient patient = Patient.builder()
                .name("Juan")
                .identity("123")
                .phone("3001111111")
                .status(PatientStatus.ACTIVO)
                .build();

        // =========================
        // ACT
        // Guardamos en la BD real (Testcontainers)
        // =========================
        Patient saved = patientRepository.save(patient);

        // =========================
        // ASSERT
        // Verificamos que se haya generado el ID
        // =========================
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void shouldFindPatientById() {

        // ARRANGE
        Patient patient = Patient.builder()
                .name("Maria")
                .identity("456")
                .phone("3002222222")
                .status(PatientStatus.ACTIVO)
                .build();

        Patient saved = patientRepository.save(patient);

        // ACT
        Patient found = patientRepository.findById(saved.getId()).orElse(null);

        // ASSERT
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Maria");
    }

    @Test
    void shouldFindAllPatients() {

        // ARRANGE
        Patient p1 = Patient.builder()
                .name("A")
                .identity("111")
                .phone("3000000001")
                .status(PatientStatus.ACTIVO)
                .build();

        Patient p2 = Patient.builder()
                .name("B")
                .identity("222")
                .phone("3000000002")
                .status(PatientStatus.ACTIVO)
                .build();

        patientRepository.save(p1);
        patientRepository.save(p2);

        // ACT
        List<Patient> patients = patientRepository.findAll();

        // ASSERT
        assertThat(patients).hasSize(2);
    }

    @Test
    void shouldFindPatientsByStatus() {

        // ARRANGE
        Patient active = Patient.builder()
                .name("Activo")
                .identity("333")
                .phone("3000000003")
                .status(PatientStatus.ACTIVO)
                .build();

        Patient inactive = Patient.builder()
                .name("Inactivo")
                .identity("444")
                .phone("3000000004")
                .status(PatientStatus.INACTIVO)
                .build();

        patientRepository.save(active);
        patientRepository.save(inactive);

        // ACT
        List<Patient> result = patientRepository.findByStatus(PatientStatus.ACTIVO);

        // ASSERT
        // Solo debe traer los activos
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getStatus()).isEqualTo(PatientStatus.ACTIVO);
    }

    @Test
    void shouldPerformLogicalDelete() {

        // ARRANGE
        Patient patient = Patient.builder()
                .name("Eliminar")
                .identity("555")
                .phone("3000000005")
                .status(PatientStatus.ACTIVO)
                .build();

        Patient saved = patientRepository.save(patient);

        // ACT
        // Simulamos borrado lógico (cambio de estado)
        saved.setStatus(PatientStatus.INACTIVO);
        patientRepository.save(saved);

        // ASSERT
        Patient updated = patientRepository.findById(saved.getId()).orElse(null);

        assertThat(updated).isNotNull();
        assertThat(updated.getStatus()).isEqualTo(PatientStatus.INACTIVO);
    }
}

 */