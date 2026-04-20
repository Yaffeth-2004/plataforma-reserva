package com.edu.plataforma_reserva.repositories;

import com.edu.plataforma_reserva.entities.DoctorSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface DoctorScheduleRepository extends JpaRepository<DoctorSchedule, UUID> {


    List<DoctorSchedule> findByDoctorId(UUID doctorId);


    // 🔹 Buscar horarios por doctor y día
    List<DoctorSchedule> findByDoctorIdAndDayOfWeek(UUID doctorId, String dayOfWeek);

    @Query("""
SELECT CASE WHEN COUNT(s) > 0 THEN true ELSE false END
FROM DoctorSchedule s
WHERE s.doctor.id = :doctorId
AND s.dayOfWeek = :dayOfWeek
AND (
    (:startTime < s.endTime AND :endTime > s.startTime)
)
""")
    boolean existsOverlappingSchedule(UUID doctorId,
                                      String dayOfWeek,
                                      LocalTime startTime,
                                      LocalTime endTime);

}
