package com.edu.plataforma_reserva.controllers;
/*
import com.edu.plataforma_reserva.dtos.CreateDoctorRequest;
import com.edu.plataforma_reserva.dtos.UpdateDoctorRequest;
import com.edu.plataforma_reserva.dtos.DoctorResponse;
import com.edu.plataforma_reserva.enums.DoctorStatus;
import com.edu.plataforma_reserva.exeptions.ResourceNotFoundException;
import com.edu.plataforma_reserva.exeptions.ConflictException;
import com.edu.plataforma_reserva.services.DoctorService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DoctorController.class)
@AutoConfigureMockMvc(addFilters = false) // 🔥 evita problemas con security
class DoctorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DoctorService doctorService;

    @Autowired
    private ObjectMapper objectMapper;

    // ✅ 1. Crear doctor OK
    @Test
    void shouldCreateDoctor() throws Exception {
        CreateDoctorRequest request = new CreateDoctorRequest();
        request.setName("Dr. Juan");
        request.setSpecialtyId(UUID.randomUUID());
        request.setStatus(DoctorStatus.ACTIVO);

        DoctorResponse response = new DoctorResponse();
        response.setId(UUID.randomUUID());
        response.setName("Dr. Juan");
        response.setSpecialtyId(request.getSpecialtyId());
        response.setSpecialtyName("Medicina General");
        response.setStatus(DoctorStatus.ACTIVE);

        when(doctorService.createDoctor(any())).thenReturn(response);

        mockMvc.perform(post("/api/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Dr. Juan"))
                .andExpect(jsonPath("$.specialtyName").value("Medicina General"));
    }

    // ❌ 2. Validación fallida
    @Test
    void shouldReturnBadRequest_whenInvalidInput() throws Exception {
        CreateDoctorRequest request = new CreateDoctorRequest();
        // vacío → rompe @NotBlank y @NotNull

        mockMvc.perform(post("/api/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // ❌ 3. Specialty no existe → 404
    @Test
    void shouldReturnNotFound_whenSpecialtyDoesNotExist() throws Exception {
        CreateDoctorRequest request = new CreateDoctorRequest();
        request.setName("Dr. Juan");
        request.setSpecialtyId(UUID.randomUUID());
        request.setStatus(DoctorStatus.ACTIVO);

        when(doctorService.createDoctor(any()))
                .thenThrow(new ResourceNotFoundException("Specialty not found"));

        mockMvc.perform(post("/api/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    // ❌ 4. Conflicto (ej: duplicado)
    @Test
    void shouldReturnConflict_whenDoctorAlreadyExists() throws Exception {
        CreateDoctorRequest request = new CreateDoctorRequest();
        request.setName("Dr. Juan");
        request.setSpecialtyId(UUID.randomUUID());
        request.setStatus(DoctorStatus.ACTIVO);

        when(doctorService.createDoctor(any()))
                .thenThrow(new ConflictException("Doctor ya existe"));

        mockMvc.perform(post("/api/doctors")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    // ✅ 5. Obtener por ID
    @Test
    void shouldReturnDoctorById() throws Exception {
        UUID id = UUID.randomUUID();

        DoctorResponse response = new DoctorResponse();
        response.setId(id);
        response.setName("Dr. Juan");
        response.setSpecialtyId(UUID.randomUUID());
        response.setSpecialtyName("Psicología");
        response.setStatus(DoctorStatus.ACTIVO);

        when(doctorService.getDoctorById(id)).thenReturn(response);

        mockMvc.perform(get("/api/doctors/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Dr. Juan"));
    }

    // ❌ 6. Doctor no existe
    @Test
    void shouldReturnNotFound_whenDoctorDoesNotExist() throws Exception {
        UUID id = UUID.randomUUID();

        when(doctorService.getDoctorById(id))
                .thenThrow(new ResourceNotFoundException("Doctor not found"));

        mockMvc.perform(get("/api/doctors/{id}", id))
                .andExpect(status().isNotFound());
    }

    // ✅ 7. Obtener todos
    @Test
    void shouldReturnAllDoctors() throws Exception {
        DoctorResponse response = new DoctorResponse();
        response.setId(UUID.randomUUID());
        response.setName("Dr. Ana");
        response.setSpecialtyId(UUID.randomUUID());
        response.setSpecialtyName("Nutrición");
        response.setStatus(DoctorStatus.ACTIVO);

        when(doctorService.getAllDoctors()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/doctors"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Dr. Ana"));
    }

    // ✅ 8. Actualizar doctor
    @Test
    void shouldUpdateDoctor() throws Exception {
        UUID id = UUID.randomUUID();

        UpdateDoctorRequest request = new UpdateDoctorRequest();
        request.setName("Dr. Actualizado");
        request.setSpecialtyId(UUID.randomUUID());
        request.setStatus(DoctorStatus.INACTIVO);

        DoctorResponse response = new DoctorResponse();
        response.setId(id);
        response.setName("Dr. Actualizado");
        response.setSpecialtyId(request.getSpecialtyId());
        response.setSpecialtyName("Fisioterapia");
        response.setStatus(DoctorStatus.INACTIVO);

        when(doctorService.updateDoctor(eq(id), any())).thenReturn(response);

        mockMvc.perform(put("/api/doctors/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Dr. Actualizado"))
                .andExpect(jsonPath("$.status").value("INACTIVE"));
    }
}

 */