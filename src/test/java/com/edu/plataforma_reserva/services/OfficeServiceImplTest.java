package com.edu.plataforma_reserva.services;

import static org.junit.jupiter.api.Assertions.*;


import com.edu.plataforma_reserva.dtos.CreateOfficeRequest;
import com.edu.plataforma_reserva.dtos.OfficeResponse;
import com.edu.plataforma_reserva.dtos.UpdateOfficeRequest;
import com.edu.plataforma_reserva.entities.Office;
import com.edu.plataforma_reserva.enums.OfficeStatus;
import com.edu.plataforma_reserva.exeptions.BusinessException;
import com.edu.plataforma_reserva.exeptions.ResourceNotFoundException;
import com.edu.plataforma_reserva.mappers.OfficeMapper;
import com.edu.plataforma_reserva.repositories.OfficeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfficeServiceImplTest {

    @Mock
    private OfficeRepository officeRepository;

    @Mock
    private OfficeMapper officeMapper;

    @InjectMocks
    private OfficeServiceImpl officeService;

    // 🔹 CREATE SUCCESS
    @Test
    void shouldCreateOfficeSuccessfully() {

        UUID id = UUID.randomUUID();

        CreateOfficeRequest request = CreateOfficeRequest.builder()
                .name("Consultorio 101")
                .location("Piso 1")
                .build();

        Office office = Office.builder()
                .name("Consultorio 101")
                .location("Piso 1")
                .build();

        Office saved = Office.builder()
                .id(id)
                .name("Consultorio 101")
                .location("Piso 1")
                .status(OfficeStatus.ACTIVO)
                .build();

        OfficeResponse response = OfficeResponse.builder()
                .id(id)
                .name("Consultorio 101")
                .location("Piso 1")
                .status(OfficeStatus.ACTIVO)
                .build();

        when(officeRepository.existsByNameIgnoreCase("Consultorio 101"))
                .thenReturn(false);

        when(officeMapper.toEntity(request)).thenReturn(office);
        when(officeRepository.save(office)).thenReturn(saved);
        when(officeMapper.toResponse(saved)).thenReturn(response);

        OfficeResponse result = officeService.createOffice(request);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Consultorio 101", result.getName());
        assertEquals("Piso 1", result.getLocation());
        assertEquals(OfficeStatus.ACTIVO, result.getStatus());

        verify(officeRepository).existsByNameIgnoreCase("Consultorio 101");
        verify(officeRepository).save(office);
    }

    // 🔹 CREATE DUPLICATE
    @Test
    void shouldThrowWhenOfficeAlreadyExists() {

        CreateOfficeRequest request = CreateOfficeRequest.builder()
                .name("Consultorio 101")
                .location("Piso 1")
                .build();

        when(officeRepository.existsByNameIgnoreCase("Consultorio 101"))
                .thenReturn(true);

        assertThrows(BusinessException.class,
                () -> officeService.createOffice(request));

        verify(officeRepository, never()).save(any());
    }

    // 🔹 GET BY ID
    @Test
    void shouldReturnOfficeById() {

        UUID id = UUID.randomUUID();

        Office office = Office.builder()
                .id(id)
                .name("Consultorio 101")
                .location("Piso 1")
                .status(OfficeStatus.ACTIVO)
                .build();

        OfficeResponse response = OfficeResponse.builder()
                .id(id)
                .name("Consultorio 101")
                .location("Piso 1")
                .status(OfficeStatus.ACTIVO)
                .build();

        when(officeRepository.findById(id)).thenReturn(Optional.of(office));
        when(officeMapper.toResponse(office)).thenReturn(response);

        OfficeResponse result = officeService.getOfficeById(id);

        assertEquals(id, result.getId());
        assertEquals("Consultorio 101", result.getName());
    }

    // 🔹 GET BY ID NOT FOUND
    @Test
    void shouldThrowWhenOfficeNotFound() {

        UUID id = UUID.randomUUID();

        when(officeRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> officeService.getOfficeById(id));
    }

    // 🔹 GET ALL
    @Test
    void shouldReturnAllOffices() {

        UUID id = UUID.randomUUID();

        Office office = Office.builder()
                .id(id)
                .name("Consultorio 101")
                .location("Piso 1")
                .status(OfficeStatus.ACTIVO)
                .build();

        OfficeResponse response = OfficeResponse.builder()
                .id(id)
                .name("Consultorio 101")
                .location("Piso 1")
                .status(OfficeStatus.ACTIVO)
                .build();

        when(officeRepository.findAll()).thenReturn(List.of(office));
        when(officeMapper.toResponse(office)).thenReturn(response);

        List<OfficeResponse> result = officeService.getAllOffices();

        assertEquals(1, result.size());
    }

    // 🔹 UPDATE
    @Test
    void shouldUpdateOfficeSuccessfully() {

        UUID id = UUID.randomUUID();

        UpdateOfficeRequest request = UpdateOfficeRequest.builder()
                .name("Consultorio 102")
                .location("Piso 2")
                .status(OfficeStatus.ACTIVO)
                .build();

        Office office = Office.builder()
                .id(id)
                .name("Consultorio 101")
                .location("Piso 1")
                .status(OfficeStatus.ACTIVO)
                .build();

        Office updated = Office.builder()
                .id(id)
                .name("Consultorio 102")
                .location("Piso 2")
                .status(OfficeStatus.ACTIVO)
                .build();

        OfficeResponse response = OfficeResponse.builder()
                .id(id)
                .name("Consultorio 102")
                .location("Piso 2")
                .status(OfficeStatus.ACTIVO)
                .build();

        when(officeRepository.findById(id)).thenReturn(Optional.of(office));
        when(officeRepository.save(office)).thenReturn(updated);
        when(officeMapper.toResponse(updated)).thenReturn(response);

        OfficeResponse result = officeService.updateOffice(id, request);

        assertEquals("Consultorio 102", result.getName());

        verify(officeMapper).updateEntityFromRequest( request,office);
    }

    // 🔹 DEACTIVATE
    @Test
    void shouldDeactivateOffice() {

        UUID id = UUID.randomUUID();

        Office office = Office.builder()
                .id(id)
                .status(OfficeStatus.ACTIVO)
                .build();

        when(officeRepository.findById(id)).thenReturn(Optional.of(office));

        officeService.deactivateOffice(id);

        assertEquals(OfficeStatus.INACTIVO, office.getStatus());
        verify(officeRepository).save(office);
    }

    // 🔹 ALREADY INACTIVE
    @Test
    void shouldThrowWhenOfficeAlreadyInactive() {

        UUID id = UUID.randomUUID();

        Office office = Office.builder()
                .id(id)
                .status(OfficeStatus.INACTIVO)
                .build();

        when(officeRepository.findById(id)).thenReturn(Optional.of(office));

        assertThrows(BusinessException.class,
                () -> officeService.deactivateOffice(id));
    }
}