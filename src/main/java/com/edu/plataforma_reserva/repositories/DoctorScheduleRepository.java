package com.edu.plataforma_reserva.repositories;

import com.edu.plataforma_reserva.entities.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, UUID> {

    // 🔹 Buscar horarios por doctor y día
    List<DoctorSchedule> findByDoctorIdAndDayOfWeek(UUID doctorId, String dayOfWeek);

}
