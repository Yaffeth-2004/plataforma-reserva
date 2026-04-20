package com.edu.plataforma_reserva.controllers;

import com.edu.plataforma_reserva.dtos.AvailabilitySlotResponse;
import com.edu.plataforma_reserva.services.AvailabilityService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.eq;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AvailabilityController.class)
@AutoConfigureMockMvc(addFilters = false)
class AvailabilityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AvailabilityService availabilityService;

    @Autowired
    private ObjectMapper objectMapper;

    // ✅ 1. Obtener disponibilidad correctamente
    @Test
    void shouldReturnAvailability() throws Exception {

        UUID doctorId = UUID.randomUUID();
        UUID appointmentTypeId = UUID.randomUUID();
        LocalDate date = LocalDate.of(2026, 4, 25);

        AvailabilitySlotResponse slot = AvailabilitySlotResponse.builder()
                .startAt(LocalDateTime.of(2026, 4, 25, 9, 0))
                .endAt(LocalDateTime.of(2026, 4, 25, 9, 30))
                .build();

        when(availabilityService.getAvailability(
                eq(doctorId),
                eq(appointmentTypeId),
                eq(date)
        )).thenReturn(List.of(slot));

        mockMvc.perform(get("/api/availability")
                        .param("doctorId", doctorId.toString())
                        .param("appointmentTypeId", appointmentTypeId.toString())
                        .param("date", "2026-04-25"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].startAt").value("2026-04-25T09:00:00"))
                .andExpect(jsonPath("$[0].endAt").value("2026-04-25T09:30:00"));
    }

    // ❌ 2. Falta parámetro doctorId
    @Test
    void shouldReturnBadRequest_whenDoctorIdMissing() throws Exception {

        UUID appointmentTypeId = UUID.randomUUID();

        mockMvc.perform(get("/api/availability")
                        .param("appointmentTypeId", appointmentTypeId.toString())
                        .param("date", "2026-04-25"))
                .andExpect(status().isBadRequest());
    }

    // ❌ 3. Falta appointmentTypeId
    @Test
    void shouldReturnBadRequest_whenAppointmentTypeIdMissing() throws Exception {

        UUID doctorId = UUID.randomUUID();

        mockMvc.perform(get("/api/availability")
                        .param("doctorId", doctorId.toString())
                        .param("date", "2026-04-25"))
                .andExpect(status().isBadRequest());
    }

    // ❌ 4. Falta date
    @Test
    void shouldReturnBadRequest_whenDateMissing() throws Exception {

        UUID doctorId = UUID.randomUUID();
        UUID appointmentTypeId = UUID.randomUUID();

        mockMvc.perform(get("/api/availability")
                        .param("doctorId", doctorId.toString())
                        .param("appointmentTypeId", appointmentTypeId.toString()))
                .andExpect(status().isBadRequest());
    }

    // ❌ 5. Fecha inválida
    @Test
    void shouldReturnBadRequest_whenInvalidDateFormat() throws Exception {

        UUID doctorId = UUID.randomUUID();
        UUID appointmentTypeId = UUID.randomUUID();

        mockMvc.perform(get("/api/availability")
                        .param("doctorId", doctorId.toString())
                        .param("appointmentTypeId", appointmentTypeId.toString())
                        .param("date", "25-04-2026")) // formato incorrecto
                .andExpect(status().isBadRequest());
    }
}