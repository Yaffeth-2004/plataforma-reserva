package com.edu.plataforma_reserva.services;

import static org.junit.jupiter.api.Assertions.*;

import com.edu.plataforma_reserva.dtos.CreateAppointmentTypeRequest;
import com.edu.plataforma_reserva.dtos.AppointmentTypeResponse;
import com.edu.plataforma_reserva.entities.AppointmentType;
import com.edu.plataforma_reserva.exeptions.BusinessException;
import com.edu.plataforma_reserva.mappers.AppointmentTypeMapper;
import com.edu.plataforma_reserva.repositories.AppointmentTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentTypeServiceImplTest {

    @Mock
    private AppointmentTypeRepository appointmentTypeRepository;

    @Mock
    private AppointmentTypeMapper appointmentTypeMapper;

    @InjectMocks
    private AppointmentTypeServiceImpl appointmentTypeService;

    // 🔹 CREATE SUCCESS
    @Test
    void shouldCreateAppointmentTypeSuccessfully() {

        UUID id = UUID.randomUUID();

        CreateAppointmentTypeRequest request = CreateAppointmentTypeRequest.builder()
                .name("Consulta General")
                .duration(30)
                .build();

        AppointmentType entity = AppointmentType.builder()
                .name("Consulta General")
                .duration(30)
                .build();

        AppointmentType saved = AppointmentType.builder()
                .id(id)
                .name("Consulta General")
                .duration(30)
                .build();

        AppointmentTypeResponse response = AppointmentTypeResponse.builder()
                .id(id)
                .name("Consulta General")
                .duration(30)
                .build();

        when(appointmentTypeRepository.existsByNameIgnoreCase("Consulta General"))
                .thenReturn(false);

        when(appointmentTypeMapper.toEntity(request)).thenReturn(entity);
        when(appointmentTypeRepository.save(entity)).thenReturn(saved);
        when(appointmentTypeMapper.toResponse(saved)).thenReturn(response);

        AppointmentTypeResponse result = appointmentTypeService.createAppointmentType(request);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Consulta General", result.getName());
        assertEquals(30, result.getDuration());

        verify(appointmentTypeRepository).existsByNameIgnoreCase("Consulta General");
        verify(appointmentTypeRepository).save(entity);
    }

    // 🔹 CREATE - INVALID DURATION
    @Test
    void shouldThrowWhenDurationIsInvalid() {

        CreateAppointmentTypeRequest request = CreateAppointmentTypeRequest.builder()
                .name("Consulta General")
                .duration(0)
                .build();

        assertThrows(BusinessException.class,
                () -> appointmentTypeService.createAppointmentType(request));

        verify(appointmentTypeRepository, never()).save(any());
    }

    // 🔹 CREATE - DUPLICATE
    @Test
    void shouldThrowWhenAppointmentTypeAlreadyExists() {

        CreateAppointmentTypeRequest request = CreateAppointmentTypeRequest.builder()
                .name("Consulta General")
                .duration(30)
                .build();

        when(appointmentTypeRepository.existsByNameIgnoreCase("Consulta General"))
                .thenReturn(true);

        assertThrows(BusinessException.class,
                () -> appointmentTypeService.createAppointmentType(request));

        verify(appointmentTypeRepository).existsByNameIgnoreCase("Consulta General");
        verify(appointmentTypeRepository, never()).save(any());
    }

    // 🔹 GET ALL
    @Test
    void shouldReturnAllAppointmentTypes() {

        UUID id = UUID.randomUUID();

        AppointmentType entity = AppointmentType.builder()
                .id(id)
                .name("Consulta General")
                .duration(30)
                .build();

        AppointmentTypeResponse response = AppointmentTypeResponse.builder()
                .id(id)
                .name("Consulta General")
                .duration(30)
                .build();

        when(appointmentTypeRepository.findAll()).thenReturn(List.of(entity));
        when(appointmentTypeMapper.toResponse(entity)).thenReturn(response);

        List<AppointmentTypeResponse> result = appointmentTypeService.getAllAppointmentTypes();

        assertEquals(1, result.size());
        assertEquals(id, result.get(0).getId());
        assertEquals("Consulta General", result.get(0).getName());

        verify(appointmentTypeRepository).findAll();
    }
}