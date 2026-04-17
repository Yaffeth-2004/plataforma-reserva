package com.edu.plataforma_reserva.repositories;

import com.edu.plataforma_reserva.entities.Appointment;

import com.edu.plataforma_reserva.enums.AppointmentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, UUID> {

    //Citas por paciente y estado
    List<Appointment> findByPatientIdAndStatus(UUID patientId, AppointmentStatus status);

    //Citas por rango de fecha
    List<Appointment> findByStartAtBetween(LocalDateTime start, LocalDateTime end);

    //doctor

    @Query("""
    SELECT COUNT(a) > 0 FROM Appointment a
    WHERE a.doctor.id = :doctorId
    AND a.status IN ('SCHEDULED', 'CONFIRMED')
    AND (a.startAt < :endAt AND a.endAt > :startAt)
""")
    boolean existsDoctorOverlap(UUID doctorId, LocalDateTime startAt, LocalDateTime endAt);

    //consultorio

    @Query("""
    SELECT COUNT(a) > 0 FROM Appointment a
    WHERE a.office.id = :officeId
    AND a.status IN ('SCHEDULED', 'CONFIRMED')
    AND (a.startAt < :endAt AND a.endAt > :startAt)
""")
    boolean existsOfficeOverlap(UUID officeId, LocalDateTime startAt, LocalDateTime endAt);


    //disponibilidad

    @Query("""
    SELECT a FROM Appointment a
    WHERE a.doctor.id = :doctorId
    AND FUNCTION('DATE', a.startAt) = :date
""")
    List<Appointment> findAppointmentsByDoctorAndDate(UUID doctorId, LocalDate date);


    //REPORTES

    //Ocupacion de consultorios

    @Query("""
    SELECT COUNT(a) FROM Appointment a
    WHERE a.office.id = :officeId
    AND a.startAt BETWEEN :start AND :end
""")
    Long countOfficeOccupancy(UUID officeId, LocalDateTime start, LocalDateTime end);

    //productividad doctores

    @Query("""
    SELECT a.doctor.id, COUNT(a)
    FROM Appointment a
    WHERE a.status = 'COMPLETED'
    GROUP BY a.doctor.id
    ORDER BY COUNT(a) DESC
""")
    List<Object[]> getDoctorProductivity();

    //pacientes con NO_SHOW

    @Query("""
    SELECT a.patient.id, COUNT(a)
    FROM Appointment a
    WHERE a.status = 'NO_SHOW'
    AND a.startAt BETWEEN :start AND :end
    GROUP BY a.patient.id
    ORDER BY COUNT(a) DESC
""")
    List<Object[]> getNoShowPatients(LocalDateTime start, LocalDateTime end);


}
