package com.edu.plataforma_reserva.controllers;
/*
import com.edu.plataforma_reserva.dtos.CreateOfficeRequest;
import com.edu.plataforma_reserva.dtos.UpdateOfficeRequest;
import com.edu.plataforma_reserva.dtos.OfficeResponse;
import com.edu.plataforma_reserva.enums.OfficeStatus;
import com.edu.plataforma_reserva.exeptions.ConflictException;
import com.edu.plataforma_reserva.exeptions.ResourceNotFoundException;
import com.edu.plataforma_reserva.services.OfficeService;
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

@WebMvcTest(OfficeController.class)
@AutoConfigureMockMvc(addFilters = false) // 🔥 evita problemas con security
class OfficeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OfficeService officeService;

    @Autowired
    private ObjectMapper objectMapper;

    // ✅ 1. Crear consultorio OK
    @Test
    void shouldCreateOffice() throws Exception {
        CreateOfficeRequest request = new CreateOfficeRequest();
        request.setName("Consultorio 101");
        request.setLocation("Bloque A");

        OfficeResponse response = new OfficeResponse();
        response.setId(UUID.randomUUID());
        response.setName("Consultorio 101");
        response.setLocation("Bloque A");
        response.setStatus(OfficeStatus.ACTIVO);

        when(officeService.createOffice(any())).thenReturn(response);

        mockMvc.perform(post("/api/offices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Consultorio 101"))
                .andExpect(jsonPath("$.location").value("Bloque A"));
    }

    // ❌ 2. Validación fallida
    @Test
    void shouldReturnBadRequest_whenInvalidInput() throws Exception {
        CreateOfficeRequest request = new CreateOfficeRequest();
        // vacío → rompe @NotBlank

        mockMvc.perform(post("/api/offices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // ❌ 3. Conflicto (duplicado)
    @Test
    void shouldReturnConflict_whenOfficeAlreadyExists() throws Exception {
        CreateOfficeRequest request = new CreateOfficeRequest();
        request.setName("Consultorio 101");
        request.setLocation("Bloque A");

        when(officeService.createOffice(any()))
                .thenThrow(new ConflictException("El consultorio ya existe"));

        mockMvc.perform(post("/api/offices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());
    }

    // ✅ 4. Obtener todos los consultorios
    @Test
    void shouldReturnAllOffices() throws Exception {
        OfficeResponse response = new OfficeResponse();
        response.setId(UUID.randomUUID());
        response.setName("Consultorio 102");
        response.setLocation("Bloque B");
        response.setStatus(OfficeStatus.ACTIVO);

        when(officeService.getAllOffices()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/offices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Consultorio 102"));
    }

    // ✅ 5. Actualizar consultorio
    @Test
    void shouldUpdateOffice() throws Exception {
        UUID id = UUID.randomUUID();

        UpdateOfficeRequest request = new UpdateOfficeRequest();
        request.setName("Consultorio Actualizado");
        request.setLocation("Bloque C");
        request.setStatus(OfficeStatus.INACTIVO);

        OfficeResponse response = new OfficeResponse();
        response.setId(id);
        response.setName("Consultorio Actualizado");
        response.setLocation("Bloque C");
        response.setStatus(OfficeStatus.INACTIVO);

        when(officeService.updateOffice(eq(id), any())).thenReturn(response);

        mockMvc.perform(put("/api/offices/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Consultorio Actualizado"))
                .andExpect(jsonPath("$.status").value("INACTIVE"));
    }

    // ❌ 6. No encontrado
    @Test
    void shouldReturnNotFound_whenOfficeDoesNotExist() throws Exception {
        UUID id = UUID.randomUUID();

        UpdateOfficeRequest request = new UpdateOfficeRequest();
        request.setName("X");

        when(officeService.updateOffice(eq(id), any()))
                .thenThrow(new ResourceNotFoundException("Office not found"));

        mockMvc.perform(put("/api/offices/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}

 */