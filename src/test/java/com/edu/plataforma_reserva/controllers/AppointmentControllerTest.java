package com.edu.plataforma_reserva.controllers;

import com.edu.plataforma_reserva.dtos.AppointmentResponse;
import com.edu.plataforma_reserva.dtos.CancelAppointmentRequest;
import com.edu.plataforma_reserva.dtos.CreateAppointmentRequest;
import com.edu.plataforma_reserva.enums.AppointmentStatus;
import com.edu.plataforma_reserva.exeptions.ResourceNotFoundException;
import com.edu.plataforma_reserva.services.AppointmentService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
@AutoConfigureMockMvc(addFilters = false)
class AppointmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentService appointmentService;

    @Autowired
    private ObjectMapper objectMapper;

    // ✅ 1. Crear cita
    @Test
    void shouldCreateAppointment() throws Exception {
        CreateAppointmentRequest request = new CreateAppointmentRequest();
        request.setPatientId(UUID.randomUUID());
        request.setDoctorId(UUID.randomUUID());
        request.setOfficeId(UUID.randomUUID());
        request.setAppointmentTypeId(UUID.randomUUID());
        request.setStartAt(LocalDateTime.now());

        AppointmentResponse response = AppointmentResponse.builder()
                .id(UUID.randomUUID())
                .status(AppointmentStatus.SCHEDULED)
                .build();

        when(appointmentService.create(any())).thenReturn(response);

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    // ❌ 2. Validación
    @Test
    void shouldReturnBadRequest_whenInvalidInput() throws Exception {
        CreateAppointmentRequest request = new CreateAppointmentRequest();

        mockMvc.perform(post("/api/appointments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // ✅ 3. Obtener por ID
    @Test
    void shouldReturnAppointmentById() throws Exception {
        UUID id = UUID.randomUUID();

        AppointmentResponse response = AppointmentResponse.builder()
                .id(id)
                .status(AppointmentStatus.SCHEDULED)
                .build();

        when(appointmentService.findById(id)).thenReturn(response);

        mockMvc.perform(get("/api/appointments/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("SCHEDULED"));
    }

    // ❌ 4. No encontrado
    @Test
    void shouldReturnNotFound_whenAppointmentDoesNotExist() throws Exception {
        UUID id = UUID.randomUUID();

        when(appointmentService.findById(id))
                .thenThrow(new ResourceNotFoundException("No existe"));

        mockMvc.perform(get("/api/appointments/{id}", id))
                .andExpect(status().isNotFound());
    }

    // ✅ 5. Obtener todos
    @Test
    void shouldReturnAllAppointments() throws Exception {
        AppointmentResponse response = AppointmentResponse.builder()
                .id(UUID.randomUUID())
                .status(AppointmentStatus.SCHEDULED)
                .build();

        when(appointmentService.findAll()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/appointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("SCHEDULED"));
    }

    // ✅ 6. Confirmar
    @Test
    void shouldConfirmAppointment() throws Exception {
        UUID id = UUID.randomUUID();

        AppointmentResponse response = AppointmentResponse.builder()
                .id(id)
                .status(AppointmentStatus.CONFIRMED)
                .build();

        when(appointmentService.confirm(id)).thenReturn(response);

        mockMvc.perform(put("/api/appointments/{id}/confirm", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONFIRMED"));
    }

    // ✅ 7. Cancelar
    @Test
    void shouldCancelAppointment() throws Exception {
        UUID id = UUID.randomUUID();

        CancelAppointmentRequest request = new CancelAppointmentRequest();
        request.setReason("Paciente no puede asistir");

        AppointmentResponse response = AppointmentResponse.builder()
                .id(id)
                .status(AppointmentStatus.CANCELLED)
                .build();

        when(appointmentService.cancel(eq(id), any())).thenReturn(response);

        mockMvc.perform(put("/api/appointments/{id}/cancel", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"));
    }

    // ✅ 8. Completar
    @Test
    void shouldCompleteAppointment() throws Exception {
        UUID id = UUID.randomUUID();

        AppointmentResponse response = AppointmentResponse.builder()
                .id(id)
                .status(AppointmentStatus.COMPLETED)
                .build();

        when(appointmentService.complete(eq(id), any()))
                .thenReturn(response);

        mockMvc.perform(put("/api/appointments/{id}/complete", id)
                        .param("observations", "Todo bien"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));
    }

    // ✅ 9. No show
    @Test
    void shouldMarkNoShow() throws Exception {
        UUID id = UUID.randomUUID();

        AppointmentResponse response = AppointmentResponse.builder()
                .id(id)
                .status(AppointmentStatus.NO_SHOW)
                .build();

        when(appointmentService.noShow(id)).thenReturn(response);

        mockMvc.perform(put("/api/appointments/{id}/no-show", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("NO_SHOW"));
    }
}