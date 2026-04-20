package com.edu.plataforma_reserva.controllers;

/*
import com.edu.plataforma_reserva.dtos.CreateSpecialtyRequest;
import com.edu.plataforma_reserva.dtos.SpecialtyResponse;
import com.edu.plataforma_reserva.services.SpecialtyService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SpecialtyController.class)
class SpecialtyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SpecialtyService specialtyService;

    @Autowired
    private ObjectMapper objectMapper;

    // ✅ 1. Crear especialidad OK
    @Test
    void shouldCreateSpecialty() throws Exception {
        CreateSpecialtyRequest request = new CreateSpecialtyRequest();
        request.setName("Medicina General");

        SpecialtyResponse response = SpecialtyResponse.builder()
                .id(UUID.randomUUID())
                .name("Medicina General")
                .build();

        when(specialtyService.createSpecialty(any())).thenReturn(response);

        mockMvc.perform(post("/api/specialties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Medicina General"));
    }

    // ❌ 2. Error de validación
    @Test
    void shouldReturnBadRequest_whenInvalidInput() throws Exception {
        CreateSpecialtyRequest request = new CreateSpecialtyRequest();
        // name null o vacío → rompe @NotBlank

        mockMvc.perform(post("/api/specialties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // ❌ 3. Conflicto (duplicado)
    @Test
    void shouldReturnConflict_whenSpecialtyAlreadyExists() throws Exception {
        CreateSpecialtyRequest request = new CreateSpecialtyRequest();
        request.setName("Medicina General");

        when(specialtyService.createSpecialty(any()))
                .thenThrow(new RuntimeException("La especialidad ya existe"));

        mockMvc.perform(post("/api/specialties")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    // ✅ 4. Obtener todas las especialidades
    @Test
    void shouldReturnAllSpecialties() throws Exception {
        SpecialtyResponse response = SpecialtyResponse.builder()
                .id(UUID.randomUUID())
                .name("Psicología")
                .build();

        when(specialtyService.getAllSpecialties()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/specialties"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Psicología"));
    }
}

 */