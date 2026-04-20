package com.edu.plataforma_reserva.repositories;

import com.edu.plataforma_reserva.entities.Doctor;
import com.edu.plataforma_reserva.enums.DoctorStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface DoctorRepository extends JpaRepository<Doctor, UUID> {

    List<Doctor> findBySpecialtyIdAndStatus(UUID specialtyId, DoctorStatus status);

}
