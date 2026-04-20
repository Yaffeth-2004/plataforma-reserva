package com.edu.plataforma_reserva.services;

import static org.junit.jupiter.api.Assertions.*;


import com.edu.plataforma_reserva.dtos.CreateSpecialtyRequest;
import com.edu.plataforma_reserva.dtos.SpecialtyResponse;
import com.edu.plataforma_reserva.entities.Specialty;
import com.edu.plataforma_reserva.exeptions.BusinessException;
import com.edu.plataforma_reserva.mappers.SpecialtyMapper;
import com.edu.plataforma_reserva.repositories.SpecialtyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(org.mockito.junit.jupiter.MockitoExtension.class)
class SpecialtyServiceImplTest {

    @Mock
    private SpecialtyRepository specialtyRepository;

    @Mock
    private SpecialtyMapper specialtyMapper;

    @InjectMocks
    private SpecialtyServiceImpl specialtyService;

    // 🔹 CREATE SUCCESS
    @Test
    void shouldCreateSpecialtySuccessfully() {

        UUID id = UUID.randomUUID();

        CreateSpecialtyRequest request = CreateSpecialtyRequest.builder()
                .name("Medicina General")
                .build();

        Specialty specialty = Specialty.builder()
                .name("Medicina General")
                .build();

        Specialty saved = Specialty.builder()
                .id(id)
                .name("Medicina General")
                .build();

        SpecialtyResponse response = SpecialtyResponse.builder()
                .id(id)
                .name("Medicina General")
                .build();

        when(specialtyRepository.existsByNameIgnoreCase("Medicina General"))
                .thenReturn(false);

        when(specialtyMapper.toEntity(request)).thenReturn(specialty);
        when(specialtyRepository.save(specialty)).thenReturn(saved);
        when(specialtyMapper.toResponse(saved)).thenReturn(response);

        SpecialtyResponse result = specialtyService.createSpecialty(request);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Medicina General", result.getName());

        verify(specialtyRepository).existsByNameIgnoreCase("Medicina General");
        verify(specialtyRepository).save(specialty);
    }

    // 🔹 CREATE DUPLICATE
    @Test
    void shouldThrowWhenSpecialtyAlreadyExists() {

        CreateSpecialtyRequest request = CreateSpecialtyRequest.builder()
                .name("Medicina General")
                .build();

        when(specialtyRepository.existsByNameIgnoreCase("Medicina General"))
                .thenReturn(true);

        assertThrows(BusinessException.class,
                () -> specialtyService.createSpecialty(request));

        verify(specialtyRepository).existsByNameIgnoreCase("Medicina General");
        verify(specialtyRepository, never()).save(any());
    }

    // 🔹 GET ALL
    @Test
    void shouldReturnAllSpecialties() {

        UUID id = UUID.randomUUID();

        Specialty specialty = Specialty.builder()
                .id(id)
                .name("Psicología")
                .build();

        SpecialtyResponse response = SpecialtyResponse.builder()
                .id(id)
                .name("Psicología")
                .build();

        when(specialtyRepository.findAll()).thenReturn(List.of(specialty));
        when(specialtyMapper.toResponse(specialty)).thenReturn(response);

        List<SpecialtyResponse> result = specialtyService.getAllSpecialties();

        assertEquals(1, result.size());
        assertEquals(id, result.get(0).getId());
        assertEquals("Psicología", result.get(0).getName());

        verify(specialtyRepository).findAll();
    }
}