package com.edu.plataforma_reserva.controllers;
/*
import com.edu.plataforma_reserva.dtos.CreatePatientRequest;
import com.edu.plataforma_reserva.dtos.PatientResponse;
import com.edu.plataforma_reserva.dtos.UpdatePatientRequest;
import com.edu.plataforma_reserva.enums.PatientStatus;
import com.edu.plataforma_reserva.services.PatientService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WithMockUser(username = "test", roles = "USER")
@WebMvcTest(PatientController.class)
class PatientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PatientService patientService;

    @Autowired
    private ObjectMapper objectMapper;

    // ✅ 1. Crear paciente OK
    @Test
    void shouldCreatePatient() throws Exception {
        CreatePatientRequest request = new CreatePatientRequest();
        request.setName("Juan");
        request.setIdentity("123");
        request.setPhone("300");

        PatientResponse response = PatientResponse.builder()
                .id(UUID.randomUUID())
                .name("Juan")
                .identity("123")
                .phone("300")
                .status(PatientStatus.ACTIVO)
                .build();

        when(patientService.createPatient(any())).thenReturn(response);

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Juan"))
                .andExpect(jsonPath("$.identity").value("123"));
    }

    // ❌ 2. Error de validación
    @Test
    void shouldReturnBadRequest_whenInvalidInput() throws Exception {
        CreatePatientRequest request = new CreatePatientRequest();
        // vacío → rompe @NotBlank

        mockMvc.perform(post("/api/patients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // ✅ 3. Obtener por ID
    @Test
    void shouldReturnPatientById() throws Exception {
        UUID id = UUID.randomUUID();

        PatientResponse response = PatientResponse.builder()
                .id(id)
                .name("Juan")
                .identity("123")
                .phone("300")
                .status(PatientStatus.ACTIVO)
                .build();

        when(patientService.getPatientById(id)).thenReturn(response);

        mockMvc.perform(get("/api/patients/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value("Juan"));
    }

    // ❌ 4. No encontrado (404)
    @Test
    void shouldReturnNotFound_whenPatientDoesNotExist() throws Exception {
        UUID id = UUID.randomUUID();

        when(patientService.getPatientById(id))
                .thenThrow(new RuntimeException("Patient not found"));

        mockMvc.perform(get("/api/patients/{id}", id))
                .andExpect(status().isNotFound());
    }

    // ✅ 5. Obtener todos
    @Test
    void shouldReturnAllPatients() throws Exception {
        PatientResponse response = PatientResponse.builder()
                .id(UUID.randomUUID())
                .name("Juan")
                .identity("123")
                .phone("300")
                .status(PatientStatus.ACTIVO)
                .build();

        when(patientService.getAllPatients()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/patients"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Juan"));
    }

    // ✅ 6. Actualizar paciente
    @Test
    void shouldUpdatePatient() throws Exception {
        UUID id = UUID.randomUUID();

        UpdatePatientRequest request = new UpdatePatientRequest();
        request.setName("Nuevo");
        request.setPhone("999");

        PatientResponse response = PatientResponse.builder()
                .id(id)
                .name("Nuevo")
                .identity("123")
                .phone("999")
                .status(PatientStatus.ACTIVO)
                .build();

        when(patientService.updatePatient(eq(id), any())).thenReturn(response);

        mockMvc.perform(put("/api/patients/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Nuevo"));
    }
}

 */