package com.edu.plataforma_reserva.services;

import com.edu.plataforma_reserva.dtos.AppointmentResponse;
import com.edu.plataforma_reserva.dtos.CancelAppointmentRequest;
import com.edu.plataforma_reserva.dtos.CreateAppointmentRequest;
import com.edu.plataforma_reserva.entities.*;
import com.edu.plataforma_reserva.enums.AppointmentStatus;
import com.edu.plataforma_reserva.enums.DoctorStatus;
import com.edu.plataforma_reserva.enums.OfficeStatus;
import com.edu.plataforma_reserva.enums.PatientStatus;
import com.edu.plataforma_reserva.exeptions.BusinessException;
import com.edu.plataforma_reserva.exeptions.ConflictException;
import com.edu.plataforma_reserva.exeptions.ResourceNotFoundException;
import com.edu.plataforma_reserva.mappers.AppointmentMapper;
import com.edu.plataforma_reserva.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final OfficeRepository officeRepository;
    private final AppointmentTypeRepository appointmentTypeRepository;
    private final DoctorScheduleRepository doctorScheduleRepository;
    private final AppointmentMapper appointmentMapper;

    // ─────────────────────────────────────────────
    // CREAR CITA
    // ─────────────────────────────────────────────

    @Override
    @Transactional
    public AppointmentResponse create(CreateAppointmentRequest request) {

        // 1. Validar que el paciente exista y esté activo
        Patient patient = patientRepository.findById(request.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Paciente no encontrado con ID: " + request.getPatientId()));

        if (patient.getStatus() != PatientStatus.ACTIVO) {
            throw new BusinessException("El paciente se encuentra inactivo y no puede recibir citas.");
        }

        // 2. Validar que el doctor exista y esté activo
        Doctor doctor = doctorRepository.findById(request.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doctor no encontrado con ID: " + request.getDoctorId()));

        if (doctor.getStatus() != DoctorStatus.ACTIVO) {
            throw new BusinessException("El doctor se encuentra inactivo y no puede recibir citas.");
        }

        // 3. Validar que el consultorio exista y esté activo
        Office office = officeRepository.findById(request.getOfficeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Consultorio no encontrado con ID: " + request.getOfficeId()));

        if (office.getStatus() != OfficeStatus.ACTIVO) {
            throw new BusinessException("El consultorio se encuentra inactivo.");
        }

        // 4. Validar que el tipo de cita exista
        AppointmentType appointmentType = appointmentTypeRepository.findById(request.getAppointmentTypeId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Tipo de cita no encontrado con ID: " + request.getAppointmentTypeId()));

        // 5. Validar que la fecha/hora no sea en el pasado
        LocalDateTime startAt = request.getStartAt();
        if (startAt.isBefore(LocalDateTime.now())) {
            throw new BusinessException("No se puede crear una cita en una fecha y hora pasada.");
        }

        // 6. Calcular endAt a partir de la duración del tipo de cita (el cliente NO lo envía)
        LocalDateTime endAt = startAt.plusMinutes(appointmentType.getDuration());

        // 7. Validar que la cita esté dentro del horario laboral del doctor
        String dayOfWeek = startAt.getDayOfWeek().name(); // Ej: "MONDAY"
        List<DoctorSchedule> schedules = doctorScheduleRepository
                .findByDoctorIdAndDayOfWeek(request.getDoctorId(), dayOfWeek);

        if (schedules.isEmpty()) {
            throw new BusinessException(
                    "El doctor no tiene horario configurado para el día: " + dayOfWeek);
        }

        LocalTime startTime = startAt.toLocalTime();
        LocalTime endTime = endAt.toLocalTime();

        boolean withinSchedule = schedules.stream().anyMatch(schedule ->
                !startTime.isBefore(schedule.getStartTime()) &&
                        !endTime.isAfter(schedule.getEndTime())
        );

        if (!withinSchedule) {
            throw new BusinessException(
                    "La cita está fuera del horario laboral configurado para el doctor.");
        }

        // 8. Validar traslape de horario para el doctor
        if (appointmentRepository.existsDoctorOverlap(request.getDoctorId(), startAt, endAt)) {
            throw new ConflictException(
                    "El doctor ya tiene una cita programada en ese rango de tiempo.");
        }

        // 9. Validar traslape de horario para el consultorio
        if (appointmentRepository.existsOfficeOverlap(request.getOfficeId(), startAt, endAt)) {
            throw new ConflictException(
                    "El consultorio ya está ocupado en ese rango de tiempo.");
        }

        // 10. Validar que el paciente no tenga dos citas activas que se crucen
        boolean patientOverlap = appointmentRepository
                .findByPatientIdAndStatus(request.getPatientId(), AppointmentStatus.SCHEDULED)
                .stream()
                .anyMatch(a -> startAt.isBefore(a.getEndAt()) && endAt.isAfter(a.getStartAt()));

        boolean patientOverlapConfirmed = appointmentRepository
                .findByPatientIdAndStatus(request.getPatientId(), AppointmentStatus.CONFIRMED)
                .stream()
                .anyMatch(a -> startAt.isBefore(a.getEndAt()) && endAt.isAfter(a.getStartAt()));

        if (patientOverlap || patientOverlapConfirmed) {
            throw new ConflictException(
                    "El paciente ya tiene una cita activa que se cruza con el horario solicitado.");
        }

        // 11. Construir y persistir la cita con estado inicial SCHEDULED
        Appointment appointment = appointmentMapper.toEntity(request);
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setOffice(office);
        appointment.setAppointmentType(appointmentType);
        appointment.setEndAt(endAt);
        appointment.setStatus(AppointmentStatus.SCHEDULED);
        appointment.setCreatedAt(LocalDateTime.now());
        appointment.setUpdatedAt(LocalDateTime.now());

        return appointmentMapper.toResponse(appointmentRepository.save(appointment));
    }

    // ─────────────────────────────────────────────
    // OBTENER POR ID
    // ─────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public AppointmentResponse findById(UUID id) {
        return appointmentMapper.toResponse(findAppointmentOrThrow(id));
    }

    // ─────────────────────────────────────────────
    // LISTAR TODAS
    // ─────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<AppointmentResponse> findAll() {
        return appointmentRepository.findAll()
                .stream()
                .map(appointmentMapper::toResponse)
                .toList();
    }

    // ─────────────────────────────────────────────
    // CONFIRMAR CITA
    // ─────────────────────────────────────────────

    @Override
    @Transactional
    public AppointmentResponse confirm(UUID id) {
        Appointment appointment = findAppointmentOrThrow(id);

        // Solo SCHEDULED puede pasar a CONFIRMED
        if (appointment.getStatus() != AppointmentStatus.SCHEDULED) {
            throw new BusinessException(
                    "Solo una cita en estado SCHEDULED puede ser confirmada. Estado actual: "
                            + appointment.getStatus());
        }

        appointment.setStatus(AppointmentStatus.CONFIRMED);
        appointment.setUpdatedAt(LocalDateTime.now());

        return appointmentMapper.toResponse(appointmentRepository.save(appointment));
    }

    // ─────────────────────────────────────────────
    // CANCELAR CITA
    // ─────────────────────────────────────────────

    @Override
    @Transactional
    public AppointmentResponse cancel(UUID id, CancelAppointmentRequest request) {
        Appointment appointment = findAppointmentOrThrow(id);

        // Solo SCHEDULED o CONFIRMED pueden cancelarse
        if (appointment.getStatus() != AppointmentStatus.SCHEDULED &&
                appointment.getStatus() != AppointmentStatus.CONFIRMED) {
            throw new BusinessException(
                    "Solo se pueden cancelar citas en estado SCHEDULED o CONFIRMED. Estado actual: "
                            + appointment.getStatus());
        }

        // El motivo es obligatorio (ya validado por @NotBlank en el DTO, pero doble seguridad)
        if (request.getReason() == null || request.getReason().isBlank()) {
            throw new BusinessException("El motivo de cancelación es obligatorio.");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        appointment.setObservations("Cancelada: " + request.getReason());
        appointment.setUpdatedAt(LocalDateTime.now());

        return appointmentMapper.toResponse(appointmentRepository.save(appointment));
    }

    // ─────────────────────────────────────────────
    // COMPLETAR CITA
    // ─────────────────────────────────────────────

    @Override
    @Transactional
    public AppointmentResponse complete(UUID id, String observations) {
        Appointment appointment = findAppointmentOrThrow(id);

        // Solo CONFIRMED puede pasar a COMPLETED
        if (appointment.getStatus() != AppointmentStatus.CONFIRMED) {
            throw new BusinessException(
                    "Solo una cita en estado CONFIRMED puede ser completada. Estado actual: "
                            + appointment.getStatus());
        }

        // No se puede completar si la hora actual es anterior al inicio
        if (LocalDateTime.now().isBefore(appointment.getStartAt())) {
            throw new BusinessException(
                    "No se puede completar una cita antes de su hora de inicio programada.");
        }

        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointment.setObservations(observations);
        appointment.setUpdatedAt(LocalDateTime.now());

        return appointmentMapper.toResponse(appointmentRepository.save(appointment));
    }

    // ─────────────────────────────────────────────
    // NO-SHOW
    // ─────────────────────────────────────────────

    @Override
    @Transactional
    public AppointmentResponse noShow(UUID id) {
        Appointment appointment = findAppointmentOrThrow(id);

        // Solo CONFIRMED puede pasar a NO_SHOW
        if (appointment.getStatus() != AppointmentStatus.CONFIRMED) {
            throw new BusinessException(
                    "Solo una cita en estado CONFIRMED puede marcarse como NO_SHOW. Estado actual: "
                            + appointment.getStatus());
        }

        // No se puede marcar NO_SHOW antes de la hora de inicio
        if (LocalDateTime.now().isBefore(appointment.getStartAt())) {
            throw new BusinessException(
                    "No se puede marcar una cita como NO_SHOW antes de su hora de inicio.");
        }

        appointment.setStatus(AppointmentStatus.NO_SHOW);
        appointment.setUpdatedAt(LocalDateTime.now());

        return appointmentMapper.toResponse(appointmentRepository.save(appointment));
    }

    // ─────────────────────────────────────────────
    // HELPER PRIVADO
    // ─────────────────────────────────────────────

    private Appointment findAppointmentOrThrow(UUID id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Cita no encontrada con ID: " + id));
    }
}
