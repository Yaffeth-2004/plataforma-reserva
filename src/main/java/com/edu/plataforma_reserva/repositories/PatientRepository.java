package com.edu.plataforma_reserva.repositories;


import com.edu.plataforma_reserva.entities.Patient;
import com.edu.plataforma_reserva.enums.PatientStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PatientRepository extends JpaRepository<Patient, UUID> {

    List<Patient> findByStatus(PatientStatus status);

}
