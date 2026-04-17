package com.edu.plataforma_reserva.repositories;

import com.edu.plataforma_reserva.entities.AppointmentType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AppointmentTypeRepository extends JpaRepository<AppointmentType, UUID> {
}
