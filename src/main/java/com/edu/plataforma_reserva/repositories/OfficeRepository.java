package com.edu.plataforma_reserva.repositories;

import com.edu.plataforma_reserva.entities.Office;
import com.edu.plataforma_reserva.enums.OfficeStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OfficeRepository extends JpaRepository<Office, UUID> {

    List<Office> findByStatus(OfficeStatus status);
    boolean existsByNameIgnoreCase(String name);

}
