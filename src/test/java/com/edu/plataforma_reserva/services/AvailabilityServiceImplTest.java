package com.edu.plataforma_reserva.services;



import com.edu.plataforma_reserva.dtos.AvailabilitySlotResponse;
import com.edu.plataforma_reserva.entities.Appointment;
import com.edu.plataforma_reserva.entities.AppointmentType;
import com.edu.plataforma_reserva.entities.DoctorSchedule;
import com.edu.plataforma_reserva.enums.AppointmentStatus;
import com.edu.plataforma_reserva.exeptions.ResourceNotFoundException;
import com.edu.plataforma_reserva.repositories.AppointmentRepository;
import com.edu.plataforma_reserva.repositories.AppointmentTypeRepository;
import com.edu.plataforma_reserva.repositories.DoctorScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias para AvailabilityServiceImpl")
class AvailabilityServiceImplTest {

    @Mock
    private DoctorScheduleRepository doctorScheduleRepository;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private AppointmentTypeRepository appointmentTypeRepository;

    @InjectMocks
    private AvailabilityServiceImpl availabilityService;

    private UUID doctorId;
    private UUID appointmentTypeId;
    private LocalDate testDate;
    private AppointmentType appointmentType;

    @BeforeEach
    void setUp() {
        doctorId = UUID.randomUUID();
        appointmentTypeId = UUID.randomUUID();
        testDate = LocalDate.of(2025, 4, 21); // Lunes

        appointmentType = new AppointmentType();
        appointmentType.setId(appointmentTypeId);
        appointmentType.setDuration(30); // 30 minutos
    }

    @Test
    @DisplayName("Debe lanzar ResourceNotFoundException si el tipo de cita no existe")
    void shouldThrowExceptionWhenAppointmentTypeNotFound() {
        when(appointmentTypeRepository.findById(appointmentTypeId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> availabilityService.getAvailability(doctorId, appointmentTypeId, testDate))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Tipo de cita no encontrado");
    }

    @Test
    @DisplayName("Debe retornar lista vacía si el doctor no tiene horario configurado para ese día")
    void shouldReturnEmptyListWhenNoScheduleForDoctor() {
        when(appointmentTypeRepository.findById(appointmentTypeId)).thenReturn(Optional.of(appointmentType));
        when(doctorScheduleRepository.findByDoctorIdAndDayOfWeek(doctorId, testDate.getDayOfWeek().name()))
                .thenReturn(List.of());

        List<AvailabilitySlotResponse> slots = availabilityService.getAvailability(doctorId, appointmentTypeId, testDate);

        assertThat(slots).isEmpty();
    }

    @Test
    @DisplayName("Debe generar todos los slots cuando no hay citas programadas")
    void shouldGenerateAllSlotsWhenNoAppointments() {
        // Configurar tipo de cita
        when(appointmentTypeRepository.findById(appointmentTypeId)).thenReturn(Optional.of(appointmentType));

        // Configurar horario del doctor de 9:00 a 10:00
        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setStartTime(LocalTime.of(9, 0));
        schedule.setEndTime(LocalTime.of(10, 0));
        when(doctorScheduleRepository.findByDoctorIdAndDayOfWeek(doctorId, testDate.getDayOfWeek().name()))
                .thenReturn(List.of(schedule));

        // Sin citas activas
        when(appointmentRepository.findAppointmentsByDoctorAndDate(doctorId, testDate))
                .thenReturn(List.of());

        List<AvailabilitySlotResponse> slots = availabilityService.getAvailability(doctorId, appointmentTypeId, testDate);

        assertThat(slots).hasSize(2);
        // Primer slot: 9:00 - 9:30
        assertThat(slots.get(0).getStartAt()).isEqualTo(LocalDateTime.of(testDate, LocalTime.of(9, 0)));
        assertThat(slots.get(0).getEndAt()).isEqualTo(LocalDateTime.of(testDate, LocalTime.of(9, 30)));
        // Segundo slot: 9:30 - 10:00
        assertThat(slots.get(1).getStartAt()).isEqualTo(LocalDateTime.of(testDate, LocalTime.of(9, 30)));
        assertThat(slots.get(1).getEndAt()).isEqualTo(LocalDateTime.of(testDate, LocalTime.of(10, 0)));
    }

    @Test
    @DisplayName("Debe filtrar los slots que solapan con citas existentes (SCHEDULED o CONFIRMED)")
    void shouldFilterSlotsThatOverlapWithExistingAppointments() {
        when(appointmentTypeRepository.findById(appointmentTypeId)).thenReturn(Optional.of(appointmentType));

        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setStartTime(LocalTime.of(9, 0));
        schedule.setEndTime(LocalTime.of(10, 30));
        when(doctorScheduleRepository.findByDoctorIdAndDayOfWeek(doctorId, testDate.getDayOfWeek().name()))
                .thenReturn(List.of(schedule));

        // Cita existente de 9:30 a 10:00
        Appointment existingAppointment = new Appointment();
        existingAppointment.setStatus(AppointmentStatus.SCHEDULED);
        existingAppointment.setStartAt(LocalDateTime.of(testDate, LocalTime.of(9, 30)));
        existingAppointment.setEndAt(LocalDateTime.of(testDate, LocalTime.of(10, 0)));

        when(appointmentRepository.findAppointmentsByDoctorAndDate(doctorId, testDate))
                .thenReturn(List.of(existingAppointment));

        List<AvailabilitySlotResponse> slots = availabilityService.getAvailability(doctorId, appointmentTypeId, testDate);

        // Slots esperados: 9:00-9:30 y 10:00-10:30 (el de 9:30-10:00 está ocupado)
        assertThat(slots).hasSize(2);
        assertThat(slots.get(0).getStartAt()).isEqualTo(LocalDateTime.of(testDate, LocalTime.of(9, 0)));
        assertThat(slots.get(1).getStartAt()).isEqualTo(LocalDateTime.of(testDate, LocalTime.of(10, 0)));
    }

    @Test
    @DisplayName("Debe ignorar citas con estado CANCELED o COMPLETED (no bloquean disponibilidad)")
    void shouldIgnoreCancelledOrCompletedAppointments() {
        when(appointmentTypeRepository.findById(appointmentTypeId)).thenReturn(Optional.of(appointmentType));

        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setStartTime(LocalTime.of(9, 0));
        schedule.setEndTime(LocalTime.of(10, 0));
        when(doctorScheduleRepository.findByDoctorIdAndDayOfWeek(doctorId, testDate.getDayOfWeek().name()))
                .thenReturn(List.of(schedule));

        // Cita cancelada en el primer slot
        Appointment cancelledAppointment = new Appointment();
        cancelledAppointment.setStatus(AppointmentStatus.CANCELLED);
        cancelledAppointment.setStartAt(LocalDateTime.of(testDate, LocalTime.of(9, 0)));
        cancelledAppointment.setEndAt(LocalDateTime.of(testDate, LocalTime.of(9, 30)));

        when(appointmentRepository.findAppointmentsByDoctorAndDate(doctorId, testDate))
                .thenReturn(List.of(cancelledAppointment));

        List<AvailabilitySlotResponse> slots = availabilityService.getAvailability(doctorId, appointmentTypeId, testDate);

        // Ambos slots deben estar disponibles porque la cita cancelada no se considera activa
        assertThat(slots).hasSize(2);
        assertThat(slots.get(0).getStartAt()).isEqualTo(LocalDateTime.of(testDate, LocalTime.of(9, 0)));
        assertThat(slots.get(1).getStartAt()).isEqualTo(LocalDateTime.of(testDate, LocalTime.of(9, 30)));
    }

    @Test
    @DisplayName("Debe manejar múltiples bloques de horario para el mismo doctor")
    void shouldHandleMultipleScheduleBlocks() {
        when(appointmentTypeRepository.findById(appointmentTypeId)).thenReturn(Optional.of(appointmentType));

        // Dos bloques: 9:00-10:00 y 11:00-12:00
        DoctorSchedule morning = new DoctorSchedule();
        morning.setStartTime(LocalTime.of(9, 0));
        morning.setEndTime(LocalTime.of(10, 0));

        DoctorSchedule afternoon = new DoctorSchedule();
        afternoon.setStartTime(LocalTime.of(11, 0));
        afternoon.setEndTime(LocalTime.of(12, 0));

        when(doctorScheduleRepository.findByDoctorIdAndDayOfWeek(doctorId, testDate.getDayOfWeek().name()))
                .thenReturn(List.of(morning, afternoon));

        when(appointmentRepository.findAppointmentsByDoctorAndDate(doctorId, testDate))
                .thenReturn(List.of());

        List<AvailabilitySlotResponse> slots = availabilityService.getAvailability(doctorId, appointmentTypeId, testDate);

        assertThat(slots).hasSize(4);
        // Mañana: 9:00-9:30, 9:30-10:00
        assertThat(slots.get(0).getStartAt()).isEqualTo(LocalDateTime.of(testDate, LocalTime.of(9, 0)));
        assertThat(slots.get(1).getStartAt()).isEqualTo(LocalDateTime.of(testDate, LocalTime.of(9, 30)));
        // Tarde: 11:00-11:30, 11:30-12:00
        assertThat(slots.get(2).getStartAt()).isEqualTo(LocalDateTime.of(testDate, LocalTime.of(11, 0)));
        assertThat(slots.get(3).getStartAt()).isEqualTo(LocalDateTime.of(testDate, LocalTime.of(11, 30)));
    }

    @Test
    @DisplayName("Debe ordenar los slots por hora de inicio ascendente")
    void shouldSortSlotsByStartTime() {
        when(appointmentTypeRepository.findById(appointmentTypeId)).thenReturn(Optional.of(appointmentType));

        // Bloques desordenados intencionalmente
        DoctorSchedule afternoon = new DoctorSchedule();
        afternoon.setStartTime(LocalTime.of(11, 0));
        afternoon.setEndTime(LocalTime.of(12, 0));

        DoctorSchedule morning = new DoctorSchedule();
        morning.setStartTime(LocalTime.of(9, 0));
        morning.setEndTime(LocalTime.of(10, 0));

        when(doctorScheduleRepository.findByDoctorIdAndDayOfWeek(doctorId, testDate.getDayOfWeek().name()))
                .thenReturn(List.of(afternoon, morning)); // Orden insertado inverso

        when(appointmentRepository.findAppointmentsByDoctorAndDate(doctorId, testDate))
                .thenReturn(List.of());

        List<AvailabilitySlotResponse> slots = availabilityService.getAvailability(doctorId, appointmentTypeId, testDate);

        assertThat(slots).hasSize(4);
        // Debe estar ordenado: 9:00, 9:30, 11:00, 11:30
        assertThat(slots.get(0).getStartAt()).isEqualTo(LocalDateTime.of(testDate, LocalTime.of(9, 0)));
        assertThat(slots.get(1).getStartAt()).isEqualTo(LocalDateTime.of(testDate, LocalTime.of(9, 30)));
        assertThat(slots.get(2).getStartAt()).isEqualTo(LocalDateTime.of(testDate, LocalTime.of(11, 0)));
        assertThat(slots.get(3).getStartAt()).isEqualTo(LocalDateTime.of(testDate, LocalTime.of(11, 30)));
    }

    @Test
    @DisplayName("Debe considerar correctamente el solapamiento parcial (cita que empieza antes y termina dentro del slot)")
    void shouldConsiderPartialOverlapWhenAppointmentStartsBeforeSlot() {
        when(appointmentTypeRepository.findById(appointmentTypeId)).thenReturn(Optional.of(appointmentType));

        DoctorSchedule schedule = new DoctorSchedule();
        schedule.setStartTime(LocalTime.of(9, 0));
        schedule.setEndTime(LocalTime.of(10, 0));
        when(doctorScheduleRepository.findByDoctorIdAndDayOfWeek(doctorId, testDate.getDayOfWeek().name()))
                .thenReturn(List.of(schedule));

        // Cita de 8:45 a 9:15 (solapa parcialmente con el primer slot)
        Appointment overlapping = new Appointment();
        overlapping.setStatus(AppointmentStatus.CONFIRMED);
        overlapping.setStartAt(LocalDateTime.of(testDate, LocalTime.of(8, 45)));
        overlapping.setEndAt(LocalDateTime.of(testDate, LocalTime.of(9, 15)));

        when(appointmentRepository.findAppointmentsByDoctorAndDate(doctorId, testDate))
                .thenReturn(List.of(overlapping));

        List<AvailabilitySlotResponse> slots = availabilityService.getAvailability(doctorId, appointmentTypeId, testDate);

        // El primer slot (9:00-9:30) debe estar ocupado, el segundo (9:30-10:00) libre
        assertThat(slots).hasSize(1);
        assertThat(slots.get(0).getStartAt()).isEqualTo(LocalDateTime.of(testDate, LocalTime.of(9, 30)));
    }
}