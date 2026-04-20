package com.edu.plataforma_reserva.services;

/*



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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("AppointmentServiceImpl - Tests Unitarios")
class AppointmentServiceImplTest {

    @Mock private AppointmentRepository appointmentRepository;
    @Mock private PatientRepository patientRepository;
    @Mock private DoctorRepository doctorRepository;
    @Mock private OfficeRepository officeRepository;
    @Mock private AppointmentTypeRepository appointmentTypeRepository;
    @Mock private DoctorScheduleRepository doctorScheduleRepository;
    @Mock private AppointmentMapper appointmentMapper;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    // ── Fixtures ──────────────────────────────────────────────────────────
    private UUID patientId, doctorId, officeId, appointmentTypeId, appointmentId;
    private Patient activePatient;
    private Doctor activeDoctor;
    private Office activeOffice;
    private AppointmentType appointmentType;
    private DoctorSchedule doctorSchedule;
    private CreateAppointmentRequest validRequest;
    private Appointment savedAppointment;
    private AppointmentResponse appointmentResponse;

    // La cita empieza mañana a las 09:00 (siempre en el futuro)
    private final LocalDateTime FUTURE_START = LocalDateTime.now().plusDays(1)
            .withHour(9).withMinute(0).withSecond(0).withNano(0);
    private final LocalDateTime FUTURE_END   = FUTURE_START.plusMinutes(30);

    @BeforeEach
    void setUp() {
        patientId         = UUID.randomUUID();
        doctorId          = UUID.randomUUID();
        officeId          = UUID.randomUUID();
        appointmentTypeId = UUID.randomUUID();
        appointmentId     = UUID.randomUUID();

        activePatient = Patient.builder()
                .id(patientId).name("Juan Pérez").status(PatientStatus.ACTIVO).build();

        activeDoctor = Doctor.builder()
                .id(doctorId).name("Dra. López").status(DoctorStatus.ACTIVO).build();

        activeOffice = Office.builder()
                .id(officeId).name("Consultorio 1").status(OfficeStatus.ACTIVO).build();

        appointmentType = AppointmentType.builder()
                .id(appointmentTypeId).name("General").duration(30).build();

        // Horario: lunes-viernes 08:00–17:00
        String dayOfWeek = FUTURE_START.getDayOfWeek().name();
        doctorSchedule = DoctorSchedule.builder()
                .id(UUID.randomUUID())
                .dayOfWeek(dayOfWeek)
                .startTime(LocalTime.of(8, 0))
                .endTime(LocalTime.of(17, 0))
                .doctor(activeDoctor)
                .build();

        validRequest = new CreateAppointmentRequest();
        validRequest.setPatientId(patientId);
        validRequest.setDoctorId(doctorId);
        validRequest.setOfficeId(officeId);
        validRequest.setAppointmentTypeId(appointmentTypeId);
        validRequest.setStartAt(FUTURE_START);

        savedAppointment = Appointment.builder()
                .id(appointmentId)
                .startAt(FUTURE_START)
                .endAt(FUTURE_END)
                .status(AppointmentStatus.SCHEDULED)
                .patient(activePatient)
                .doctor(activeDoctor)
                .office(activeOffice)
                .appointmentType(appointmentType)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        appointmentResponse = AppointmentResponse.builder()
                .id(appointmentId)
                .startAt(FUTURE_START)
                .endAt(FUTURE_END)
                .status(AppointmentStatus.SCHEDULED)
                .patientId(patientId).patientName("Juan Pérez")
                .doctorId(doctorId).doctorName("Dra. López")
                .officeId(officeId).officeName("Consultorio 1")
                .appointmentTypeId(appointmentTypeId).appointmentTypeName("General")
                .build();
    }

    // ══════════════════════════════════════════════════════════════════════
    // CREATE
    // ══════════════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("create()")
    class CreateTests {

        @Test
        @DisplayName("Debe crear una cita válida con estado SCHEDULED")
        void shouldCreateAppointmentSuccessfully() {
            when(patientRepository.findById(patientId)).thenReturn(Optional.of(activePatient));
            when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(activeDoctor));
            when(officeRepository.findById(officeId)).thenReturn(Optional.of(activeOffice));
            when(appointmentTypeRepository.findById(appointmentTypeId)).thenReturn(Optional.of(appointmentType));
            when(doctorScheduleRepository.findByDoctorIdAndDayOfWeek(eq(doctorId), any()))
                    .thenReturn(List.of(doctorSchedule));
            when(appointmentRepository.existsDoctorOverlap(eq(doctorId), any(), any())).thenReturn(false);
            when(appointmentRepository.existsOfficeOverlap(eq(officeId), any(), any())).thenReturn(false);
            when(appointmentRepository.findByPatientIdAndStatus(patientId, AppointmentStatus.SCHEDULED))
                    .thenReturn(Collections.emptyList());
            when(appointmentRepository.findByPatientIdAndStatus(patientId, AppointmentStatus.CONFIRMED))
                    .thenReturn(Collections.emptyList());
            when(appointmentMapper.toEntity(validRequest)).thenReturn(savedAppointment);
            when(appointmentRepository.save(any(Appointment.class))).thenReturn(savedAppointment);
            when(appointmentMapper.toResponse(savedAppointment)).thenReturn(appointmentResponse);

            AppointmentResponse result = appointmentService.create(validRequest);

            assertThat(result).isNotNull();
            assertThat(result.getStatus()).isEqualTo(AppointmentStatus.SCHEDULED);
            verify(appointmentRepository).save(any(Appointment.class));
        }

        @Test
        @DisplayName("Debe lanzar ResourceNotFoundException si el paciente no existe")
        void shouldThrowWhenPatientNotFound() {
            when(patientRepository.findById(patientId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> appointmentService.create(validRequest))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Paciente no encontrado");
        }

        @Test
        @DisplayName("Debe lanzar BusinessException si el paciente está inactivo")
        void shouldThrowWhenPatientInactive() {
            activePatient.setStatus(PatientStatus.INACTIVO);
            when(patientRepository.findById(patientId)).thenReturn(Optional.of(activePatient));

            assertThatThrownBy(() -> appointmentService.create(validRequest))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("paciente se encuentra inactivo");
        }

        @Test
        @DisplayName("Debe lanzar ResourceNotFoundException si el doctor no existe")
        void shouldThrowWhenDoctorNotFound() {
            when(patientRepository.findById(patientId)).thenReturn(Optional.of(activePatient));
            when(doctorRepository.findById(doctorId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> appointmentService.create(validRequest))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Doctor no encontrado");
        }

        @Test
        @DisplayName("Debe lanzar BusinessException si el doctor está inactivo")
        void shouldThrowWhenDoctorInactive() {
            activeDoctor.setStatus(DoctorStatus.INACTIVO);
            when(patientRepository.findById(patientId)).thenReturn(Optional.of(activePatient));
            when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(activeDoctor));

            assertThatThrownBy(() -> appointmentService.create(validRequest))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("doctor se encuentra inactivo");
        }

        @Test
        @DisplayName("Debe lanzar ResourceNotFoundException si el consultorio no existe")
        void shouldThrowWhenOfficeNotFound() {
            when(patientRepository.findById(patientId)).thenReturn(Optional.of(activePatient));
            when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(activeDoctor));
            when(officeRepository.findById(officeId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> appointmentService.create(validRequest))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Consultorio no encontrado");
        }

        @Test
        @DisplayName("Debe lanzar BusinessException si el consultorio está inactivo")
        void shouldThrowWhenOfficeInactive() {
            activeOffice.setStatus(OfficeStatus.INACTIVO);
            when(patientRepository.findById(patientId)).thenReturn(Optional.of(activePatient));
            when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(activeDoctor));
            when(officeRepository.findById(officeId)).thenReturn(Optional.of(activeOffice));

            assertThatThrownBy(() -> appointmentService.create(validRequest))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("consultorio se encuentra inactivo");
        }

        @Test
        @DisplayName("Debe lanzar BusinessException si la fecha de inicio es en el pasado")
        void shouldThrowWhenStartAtIsInThePast() {
            validRequest.setStartAt(LocalDateTime.now().minusHours(1));
            when(patientRepository.findById(patientId)).thenReturn(Optional.of(activePatient));
            when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(activeDoctor));
            when(officeRepository.findById(officeId)).thenReturn(Optional.of(activeOffice));
            when(appointmentTypeRepository.findById(appointmentTypeId)).thenReturn(Optional.of(appointmentType));

            assertThatThrownBy(() -> appointmentService.create(validRequest))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("fecha y hora pasada");
        }

        @Test
        @DisplayName("Debe lanzar BusinessException si el doctor no tiene horario ese día")
        void shouldThrowWhenDoctorHasNoScheduleForDay() {
            when(patientRepository.findById(patientId)).thenReturn(Optional.of(activePatient));
            when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(activeDoctor));
            when(officeRepository.findById(officeId)).thenReturn(Optional.of(activeOffice));
            when(appointmentTypeRepository.findById(appointmentTypeId)).thenReturn(Optional.of(appointmentType));
            when(doctorScheduleRepository.findByDoctorIdAndDayOfWeek(eq(doctorId), any()))
                    .thenReturn(Collections.emptyList());

            assertThatThrownBy(() -> appointmentService.create(validRequest))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("no tiene horario configurado");
        }

        @Test
        @DisplayName("Debe lanzar BusinessException si la cita está fuera del horario laboral")
        void shouldThrowWhenAppointmentOutsideWorkingHours() {
            // Horario 08:00–09:00, cita a las 09:00 por 30 min → termina 09:30 → fuera
            DoctorSchedule narrowSchedule = DoctorSchedule.builder()
                    .dayOfWeek(FUTURE_START.getDayOfWeek().name())
                    .startTime(LocalTime.of(8, 0))
                    .endTime(LocalTime.of(9, 0))
                    .doctor(activeDoctor)
                    .build();

            when(patientRepository.findById(patientId)).thenReturn(Optional.of(activePatient));
            when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(activeDoctor));
            when(officeRepository.findById(officeId)).thenReturn(Optional.of(activeOffice));
            when(appointmentTypeRepository.findById(appointmentTypeId)).thenReturn(Optional.of(appointmentType));
            when(doctorScheduleRepository.findByDoctorIdAndDayOfWeek(eq(doctorId), any()))
                    .thenReturn(List.of(narrowSchedule));

            assertThatThrownBy(() -> appointmentService.create(validRequest))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("fuera del horario laboral");
        }

        @Test
        @DisplayName("Debe lanzar ConflictException si hay traslape con otra cita del doctor")
        void shouldThrowWhenDoctorOverlapExists() {
            when(patientRepository.findById(patientId)).thenReturn(Optional.of(activePatient));
            when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(activeDoctor));
            when(officeRepository.findById(officeId)).thenReturn(Optional.of(activeOffice));
            when(appointmentTypeRepository.findById(appointmentTypeId)).thenReturn(Optional.of(appointmentType));
            when(doctorScheduleRepository.findByDoctorIdAndDayOfWeek(eq(doctorId), any()))
                    .thenReturn(List.of(doctorSchedule));
            when(appointmentRepository.existsDoctorOverlap(eq(doctorId), any(), any())).thenReturn(true);

            assertThatThrownBy(() -> appointmentService.create(validRequest))
                    .isInstanceOf(ConflictException.class)
                    .hasMessageContaining("doctor ya tiene una cita");
        }

        @Test
        @DisplayName("Debe lanzar ConflictException si hay traslape con otra cita del consultorio")
        void shouldThrowWhenOfficeOverlapExists() {
            when(patientRepository.findById(patientId)).thenReturn(Optional.of(activePatient));
            when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(activeDoctor));
            when(officeRepository.findById(officeId)).thenReturn(Optional.of(activeOffice));
            when(appointmentTypeRepository.findById(appointmentTypeId)).thenReturn(Optional.of(appointmentType));
            when(doctorScheduleRepository.findByDoctorIdAndDayOfWeek(eq(doctorId), any()))
                    .thenReturn(List.of(doctorSchedule));
            when(appointmentRepository.existsDoctorOverlap(eq(doctorId), any(), any())).thenReturn(false);
            when(appointmentRepository.existsOfficeOverlap(eq(officeId), any(), any())).thenReturn(true);

            assertThatThrownBy(() -> appointmentService.create(validRequest))
                    .isInstanceOf(ConflictException.class)
                    .hasMessageContaining("consultorio ya está ocupado");
        }

        @Test
        @DisplayName("Debe lanzar ConflictException si el paciente tiene cita activa que se cruza")
        void shouldThrowWhenPatientHasOverlappingActiveAppointment() {
            // Cita activa del paciente que se cruza con la nueva
            Appointment existingAppointment = Appointment.builder()
                    .id(UUID.randomUUID())
                    .startAt(FUTURE_START.minusMinutes(15))
                    .endAt(FUTURE_START.plusMinutes(15))
                    .status(AppointmentStatus.SCHEDULED)
                    .build();

            when(patientRepository.findById(patientId)).thenReturn(Optional.of(activePatient));
            when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(activeDoctor));
            when(officeRepository.findById(officeId)).thenReturn(Optional.of(activeOffice));
            when(appointmentTypeRepository.findById(appointmentTypeId)).thenReturn(Optional.of(appointmentType));
            when(doctorScheduleRepository.findByDoctorIdAndDayOfWeek(eq(doctorId), any()))
                    .thenReturn(List.of(doctorSchedule));
            when(appointmentRepository.existsDoctorOverlap(any(), any(), any())).thenReturn(false);
            when(appointmentRepository.existsOfficeOverlap(any(), any(), any())).thenReturn(false);
            when(appointmentRepository.findByPatientIdAndStatus(patientId, AppointmentStatus.SCHEDULED))
                    .thenReturn(List.of(existingAppointment));
            when(appointmentRepository.findByPatientIdAndStatus(patientId, AppointmentStatus.CONFIRMED))
                    .thenReturn(Collections.emptyList());

            assertThatThrownBy(() -> appointmentService.create(validRequest))
                    .isInstanceOf(ConflictException.class)
                    .hasMessageContaining("paciente ya tiene una cita activa");
        }

        @Test
        @DisplayName("Debe calcular endAt automáticamente según la duración del tipo de cita")
        void shouldCalculateEndAtFromAppointmentTypeDuration() {
            when(patientRepository.findById(patientId)).thenReturn(Optional.of(activePatient));
            when(doctorRepository.findById(doctorId)).thenReturn(Optional.of(activeDoctor));
            when(officeRepository.findById(officeId)).thenReturn(Optional.of(activeOffice));
            when(appointmentTypeRepository.findById(appointmentTypeId)).thenReturn(Optional.of(appointmentType));
            when(doctorScheduleRepository.findByDoctorIdAndDayOfWeek(eq(doctorId), any()))
                    .thenReturn(List.of(doctorSchedule));
            when(appointmentRepository.existsDoctorOverlap(any(), any(), any())).thenReturn(false);
            when(appointmentRepository.existsOfficeOverlap(any(), any(), any())).thenReturn(false);
            when(appointmentRepository.findByPatientIdAndStatus(patientId, AppointmentStatus.SCHEDULED))
                    .thenReturn(Collections.emptyList());
            when(appointmentRepository.findByPatientIdAndStatus(patientId, AppointmentStatus.CONFIRMED))
                    .thenReturn(Collections.emptyList());
            when(appointmentMapper.toEntity(validRequest)).thenReturn(savedAppointment);
            when(appointmentRepository.save(any(Appointment.class))).thenAnswer(inv -> inv.getArgument(0));
            when(appointmentMapper.toResponse(any())).thenReturn(appointmentResponse);

            appointmentService.create(validRequest);

            // Verificamos que el endAt seteado sea exactamente startAt + 30 min
            verify(appointmentRepository).save(argThat(a ->
                    a.getEndAt().equals(FUTURE_START.plusMinutes(30))
            ));
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // FIND BY ID
    // ══════════════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("findById()")
    class FindByIdTests {

        @Test
        @DisplayName("Debe retornar la cita cuando existe")
        void shouldReturnAppointmentWhenExists() {
            when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(savedAppointment));
            when(appointmentMapper.toResponse(savedAppointment)).thenReturn(appointmentResponse);

            AppointmentResponse result = appointmentService.findById(appointmentId);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(appointmentId);
        }

        @Test
        @DisplayName("Debe lanzar ResourceNotFoundException si la cita no existe")
        void shouldThrowWhenAppointmentNotFound() {
            when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> appointmentService.findById(appointmentId))
                    .isInstanceOf(ResourceNotFoundException.class)
                    .hasMessageContaining("Cita no encontrada");
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // FIND ALL
    // ══════════════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("findAll()")
    class FindAllTests {

        @Test
        @DisplayName("Debe retornar lista de citas")
        void shouldReturnAllAppointments() {
            when(appointmentRepository.findAll()).thenReturn(List.of(savedAppointment));
            when(appointmentMapper.toResponse(savedAppointment)).thenReturn(appointmentResponse);

            List<AppointmentResponse> result = appointmentService.findAll();

            assertThat(result).hasSize(1);
        }

        @Test
        @DisplayName("Debe retornar lista vacía si no hay citas")
        void shouldReturnEmptyListWhenNoAppointments() {
            when(appointmentRepository.findAll()).thenReturn(Collections.emptyList());

            List<AppointmentResponse> result = appointmentService.findAll();

            assertThat(result).isEmpty();
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // CONFIRM
    // ══════════════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("confirm()")
    class ConfirmTests {

        @Test
        @DisplayName("Debe confirmar una cita en estado SCHEDULED")
        void shouldConfirmScheduledAppointment() {
            savedAppointment.setStatus(AppointmentStatus.SCHEDULED);
            when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(savedAppointment));
            when(appointmentRepository.save(any())).thenReturn(savedAppointment);
            when(appointmentMapper.toResponse(any())).thenReturn(appointmentResponse);

            appointmentService.confirm(appointmentId);

            verify(appointmentRepository).save(argThat(a ->
                    a.getStatus() == AppointmentStatus.CONFIRMED));
        }

        @Test
        @DisplayName("Debe lanzar BusinessException al confirmar una cita CONFIRMED")
        void shouldThrowWhenConfirmingAlreadyConfirmed() {
            savedAppointment.setStatus(AppointmentStatus.CONFIRMED);
            when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(savedAppointment));

            assertThatThrownBy(() -> appointmentService.confirm(appointmentId))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("SCHEDULED");
        }

        @Test
        @DisplayName("Debe lanzar BusinessException al confirmar una cita CANCELLED")
        void shouldThrowWhenConfirmingCancelled() {
            savedAppointment.setStatus(AppointmentStatus.CANCELLED);
            when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(savedAppointment));

            assertThatThrownBy(() -> appointmentService.confirm(appointmentId))
                    .isInstanceOf(BusinessException.class);
        }

        @Test
        @DisplayName("Debe lanzar BusinessException al confirmar una cita COMPLETED")
        void shouldThrowWhenConfirmingCompleted() {
            savedAppointment.setStatus(AppointmentStatus.COMPLETED);
            when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(savedAppointment));

            assertThatThrownBy(() -> appointmentService.confirm(appointmentId))
                    .isInstanceOf(BusinessException.class);
        }

        @Test
        @DisplayName("Debe lanzar BusinessException al confirmar una cita NO_SHOW")
        void shouldThrowWhenConfirmingNoShow() {
            savedAppointment.setStatus(AppointmentStatus.NO_SHOW);
            when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(savedAppointment));

            assertThatThrownBy(() -> appointmentService.confirm(appointmentId))
                    .isInstanceOf(BusinessException.class);
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // CANCEL
    // ══════════════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("cancel()")
    class CancelTests {

        private CancelAppointmentRequest cancelRequest;

        @BeforeEach
        void setUp() {
            cancelRequest = new CancelAppointmentRequest();
            cancelRequest.setReason("Paciente no puede asistir");
        }

        @Test
        @DisplayName("Debe cancelar una cita en estado SCHEDULED")
        void shouldCancelScheduledAppointment() {
            savedAppointment.setStatus(AppointmentStatus.SCHEDULED);
            when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(savedAppointment));
            when(appointmentRepository.save(any())).thenReturn(savedAppointment);
            when(appointmentMapper.toResponse(any())).thenReturn(appointmentResponse);

            appointmentService.cancel(appointmentId, cancelRequest);

            verify(appointmentRepository).save(argThat(a ->
                    a.getStatus() == AppointmentStatus.CANCELLED));
        }

        @Test
        @DisplayName("Debe cancelar una cita en estado CONFIRMED")
        void shouldCancelConfirmedAppointment() {
            savedAppointment.setStatus(AppointmentStatus.CONFIRMED);
            when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(savedAppointment));
            when(appointmentRepository.save(any())).thenReturn(savedAppointment);
            when(appointmentMapper.toResponse(any())).thenReturn(appointmentResponse);

            appointmentService.cancel(appointmentId, cancelRequest);

            verify(appointmentRepository).save(argThat(a ->
                    a.getStatus() == AppointmentStatus.CANCELLED));
        }

        @Test
        @DisplayName("Debe lanzar BusinessException al cancelar una cita COMPLETED")
        void shouldThrowWhenCancellingCompleted() {
            savedAppointment.setStatus(AppointmentStatus.COMPLETED);
            when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(savedAppointment));

            assertThatThrownBy(() -> appointmentService.cancel(appointmentId, cancelRequest))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("SCHEDULED o CONFIRMED");
        }

        @Test
        @DisplayName("Debe lanzar BusinessException al cancelar una cita NO_SHOW")
        void shouldThrowWhenCancellingNoShow() {
            savedAppointment.setStatus(AppointmentStatus.NO_SHOW);
            when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(savedAppointment));

            assertThatThrownBy(() -> appointmentService.cancel(appointmentId, cancelRequest))
                    .isInstanceOf(BusinessException.class);
        }

        @Test
        @DisplayName("Debe registrar el motivo de cancelación en observations")
        void shouldSaveReasonInObservations() {
            savedAppointment.setStatus(AppointmentStatus.SCHEDULED);
            when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(savedAppointment));
            when(appointmentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
            when(appointmentMapper.toResponse(any())).thenReturn(appointmentResponse);

            appointmentService.cancel(appointmentId, cancelRequest);

            verify(appointmentRepository).save(argThat(a ->
                    a.getObservations() != null &&
                            a.getObservations().contains("Paciente no puede asistir")));
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // COMPLETE
    // ══════════════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("complete()")
    class CompleteTests {

        @Test
        @DisplayName("Debe completar una cita CONFIRMED cuya hora de inicio ya pasó")
        void shouldCompleteConfirmedAppointment() {
            savedAppointment.setStatus(AppointmentStatus.CONFIRMED);
            // Inicio en el pasado para que la validación de hora pase
            savedAppointment.setStartAt(LocalDateTime.now().minusMinutes(10));

            when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(savedAppointment));
            when(appointmentRepository.save(any())).thenReturn(savedAppointment);
            when(appointmentMapper.toResponse(any())).thenReturn(appointmentResponse);

            appointmentService.complete(appointmentId, "Sin novedades");

            verify(appointmentRepository).save(argThat(a ->
                    a.getStatus() == AppointmentStatus.COMPLETED));
        }

        @Test
        @DisplayName("Debe lanzar BusinessException al completar una cita SCHEDULED")
        void shouldThrowWhenCompletingScheduled() {
            savedAppointment.setStatus(AppointmentStatus.SCHEDULED);
            when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(savedAppointment));

            assertThatThrownBy(() -> appointmentService.complete(appointmentId, null))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("CONFIRMED");
        }

        @Test
        @DisplayName("Debe lanzar BusinessException si la hora actual es anterior al inicio")
        void shouldThrowWhenCompletingBeforeStartTime() {
            savedAppointment.setStatus(AppointmentStatus.CONFIRMED);
            // Inicio en el futuro: aún no ha comenzado
            savedAppointment.setStartAt(LocalDateTime.now().plusMinutes(30));

            when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(savedAppointment));

            assertThatThrownBy(() -> appointmentService.complete(appointmentId, null))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("anterior al inicio");
        }

        @Test
        @DisplayName("Debe guardar observaciones al completar la cita")
        void shouldSaveObservationsOnComplete() {
            savedAppointment.setStatus(AppointmentStatus.CONFIRMED);
            savedAppointment.setStartAt(LocalDateTime.now().minusMinutes(5));

            when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(savedAppointment));
            when(appointmentRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
            when(appointmentMapper.toResponse(any())).thenReturn(appointmentResponse);

            appointmentService.complete(appointmentId, "Paciente atendido correctamente");

            verify(appointmentRepository).save(argThat(a ->
                    "Paciente atendido correctamente".equals(a.getObservations())));
        }
    }

    // ══════════════════════════════════════════════════════════════════════
    // NO-SHOW
    // ══════════════════════════════════════════════════════════════════════
    @Nested
    @DisplayName("noShow()")
    class NoShowTests {

        @Test
        @DisplayName("Debe marcar NO_SHOW una cita CONFIRMED cuya hora ya pasó")
        void shouldMarkNoShowForConfirmedAppointment() {
            savedAppointment.setStatus(AppointmentStatus.CONFIRMED);
            savedAppointment.setStartAt(LocalDateTime.now().minusMinutes(10));

            when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(savedAppointment));
            when(appointmentRepository.save(any())).thenReturn(savedAppointment);
            when(appointmentMapper.toResponse(any())).thenReturn(appointmentResponse);

            appointmentService.noShow(appointmentId);

            verify(appointmentRepository).save(argThat(a ->
                    a.getStatus() == AppointmentStatus.NO_SHOW));
        }

        @Test
        @DisplayName("Debe lanzar BusinessException al marcar NO_SHOW una cita SCHEDULED")
        void shouldThrowWhenNoShowOnScheduled() {
            savedAppointment.setStatus(AppointmentStatus.SCHEDULED);
            when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(savedAppointment));

            assertThatThrownBy(() -> appointmentService.noShow(appointmentId))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("CONFIRMED");
        }

        @Test
        @DisplayName("Debe lanzar BusinessException si se marca NO_SHOW antes de la hora de inicio")
        void shouldThrowWhenNoShowBeforeStartTime() {
            savedAppointment.setStatus(AppointmentStatus.CONFIRMED);
            savedAppointment.setStartAt(LocalDateTime.now().plusMinutes(30));

            when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(savedAppointment));

            assertThatThrownBy(() -> appointmentService.noShow(appointmentId))
                    .isInstanceOf(BusinessException.class)
                    .hasMessageContaining("antes de su hora de inicio");
        }

        @Test
        @DisplayName("Debe lanzar BusinessException al marcar NO_SHOW una cita COMPLETED")
        void shouldThrowWhenNoShowOnCompleted() {
            savedAppointment.setStatus(AppointmentStatus.COMPLETED);
            when(appointmentRepository.findById(appointmentId)).thenReturn(Optional.of(savedAppointment));

            assertThatThrownBy(() -> appointmentService.noShow(appointmentId))
                    .isInstanceOf(BusinessException.class);
        }
    }
}

 */