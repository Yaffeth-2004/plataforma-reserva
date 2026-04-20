package com.edu.plataforma_reserva.services;


import com.edu.plataforma_reserva.dtos.DoctorProductivityResponse;
import com.edu.plataforma_reserva.dtos.NoShowPatientResponse;
import com.edu.plataforma_reserva.dtos.OfficeOccupancyResponse;
import com.edu.plataforma_reserva.entities.Doctor;
import com.edu.plataforma_reserva.entities.Office;
import com.edu.plataforma_reserva.entities.Patient;
import com.edu.plataforma_reserva.exeptions.BusinessException;
import com.edu.plataforma_reserva.repositories.AppointmentRepository;
import com.edu.plataforma_reserva.repositories.DoctorRepository;
import com.edu.plataforma_reserva.repositories.OfficeRepository;
import com.edu.plataforma_reserva.repositories.PatientRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Pruebas unitarias para ReportServiceImpl")
class ReportServiceImplTest {

    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private OfficeRepository officeRepository;
    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private ReportServiceImpl reportService;

    private final LocalDateTime start = LocalDateTime.of(2025, 4, 1, 0, 0);
    private final LocalDateTime end = LocalDateTime.of(2025, 4, 30, 23, 59);

    @Test
    @DisplayName("getOfficeOccupancy - debe retornar lista con ocupación de cada consultorio")
    void shouldReturnOfficeOccupancy() {
        // given
        UUID officeId1 = UUID.randomUUID();
        UUID officeId2 = UUID.randomUUID();
        Office office1 = Office.builder().id(officeId1).name("Consultorio A").build();
        Office office2 = Office.builder().id(officeId2).name("Consultorio B").build();
        when(officeRepository.findAll()).thenReturn(List.of(office1, office2));
        when(appointmentRepository.countOfficeOccupancy(officeId1, start, end)).thenReturn(5L);
        when(appointmentRepository.countOfficeOccupancy(officeId2, start, end)).thenReturn(0L);

        // when
        List<OfficeOccupancyResponse> result = reportService.getOfficeOccupancy(start, end);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getOfficeId()).isEqualTo(officeId1);
        assertThat(result.get(0).getTotalAppointments()).isEqualTo(5L);
        assertThat(result.get(1).getOfficeId()).isEqualTo(officeId2);
        assertThat(result.get(1).getTotalAppointments()).isZero();
    }

    @Test
    @DisplayName("getOfficeOccupancy - debe lanzar excepción si fechas inválidas")
    void shouldThrowWhenInvalidDateRange() {
        assertThatThrownBy(() -> reportService.getOfficeOccupancy(end, start))
                .isInstanceOf(BusinessException.class)
                .hasMessage("La fecha de inicio no puede ser posterior a la fecha de fin");

        assertThatThrownBy(() -> reportService.getOfficeOccupancy(null, end))
                .isInstanceOf(BusinessException.class);
    }

    @Test
    @DisplayName("getDoctorProductivity - debe retornar lista ordenada por citas completadas")
    void shouldReturnDoctorProductivity() {
        // given
        UUID doctorId1 = UUID.randomUUID();
        UUID doctorId2 = UUID.randomUUID();
        List<Object[]> mockResults = List.of(
                new Object[]{doctorId1, 10L},
                new Object[]{doctorId2, 5L}
        );
        when(appointmentRepository.getDoctorProductivity()).thenReturn(mockResults);
        when(doctorRepository.findById(doctorId1)).thenReturn(Optional.of(Doctor.builder().name("Dra. Gómez").build()));
        when(doctorRepository.findById(doctorId2)).thenReturn(Optional.of(Doctor.builder().name("Dr. Pérez").build()));

        // when
        List<DoctorProductivityResponse> result = reportService.getDoctorProductivity();

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getDoctorId()).isEqualTo(doctorId1);
        assertThat(result.get(0).getCompletedAppointments()).isEqualTo(10L);
        assertThat(result.get(0).getDoctorName()).isEqualTo("Dra. Gómez");
        assertThat(result.get(1).getDoctorId()).isEqualTo(doctorId2);
        assertThat(result.get(1).getCompletedAppointments()).isEqualTo(5L);
        assertThat(result.get(1).getDoctorName()).isEqualTo("Dr. Pérez");
    }

    @Test
    @DisplayName("getDoctorProductivity - si no hay citas completadas, retorna lista vacía")
    void shouldReturnEmptyListWhenNoCompletedAppointments() {
        when(appointmentRepository.getDoctorProductivity()).thenReturn(List.of());
        List<DoctorProductivityResponse> result = reportService.getDoctorProductivity();
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("getNoShowPatients - debe retornar pacientes con NO_SHOW en el rango")
    void shouldReturnNoShowPatients() {
        // given
        UUID patientId1 = UUID.randomUUID();
        UUID patientId2 = UUID.randomUUID();
        List<Object[]> mockResults = List.of(
                new Object[]{patientId1, 3L},
                new Object[]{patientId2, 1L}
        );
        when(appointmentRepository.getNoShowPatients(start, end)).thenReturn(mockResults);
        when(patientRepository.findById(patientId1)).thenReturn(Optional.of(Patient.builder().name("Juan Pérez").build()));
        when(patientRepository.findById(patientId2)).thenReturn(Optional.of(Patient.builder().name("Ana López").build()));

        // when
        List<NoShowPatientResponse> result = reportService.getNoShowPatients(start, end);

        // then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getPatientId()).isEqualTo(patientId1);
        assertThat(result.get(0).getTotalNoShows()).isEqualTo(3L);
        assertThat(result.get(0).getPatientName()).isEqualTo("Juan Pérez");
        assertThat(result.get(1).getPatientId()).isEqualTo(patientId2);
        assertThat(result.get(1).getTotalNoShows()).isEqualTo(1L);
        assertThat(result.get(1).getPatientName()).isEqualTo("Ana López");
    }

    @Test
    @DisplayName("getNoShowPatients - debe lanzar excepción si fechas nulas")
    void shouldThrowWhenDatesNull() {
        assertThatThrownBy(() -> reportService.getNoShowPatients(null, end))
                .isInstanceOf(BusinessException.class);
    }
}