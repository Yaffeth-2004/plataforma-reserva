package com.edu.plataforma_reserva.services;


import com.edu.plataforma_reserva.dtos.CreateOfficeRequest;
import com.edu.plataforma_reserva.dtos.OfficeResponse;
import com.edu.plataforma_reserva.dtos.UpdateOfficeRequest;
import com.edu.plataforma_reserva.entities.Office;
import com.edu.plataforma_reserva.enums.OfficeStatus;
import com.edu.plataforma_reserva.exeptions.BusinessException;
import com.edu.plataforma_reserva.exeptions.ResourceNotFoundException;
import com.edu.plataforma_reserva.mappers.OfficeMapper;
import com.edu.plataforma_reserva.repositories.OfficeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
 @RequiredArgsConstructor
public class OfficeServiceImpl implements OfficeService {

    private final OfficeRepository officeRepository;
    private final OfficeMapper officeMapper;


    // 🔹 CREATE
    @Override
    public OfficeResponse createOffice(CreateOfficeRequest request) {

        // 🔥 Validación (recomendada)
        if (officeRepository.existsByNameIgnoreCase(request.getName())) {
            throw new BusinessException("El consultorio ya existe");
        }

        Office office = officeMapper.toEntity(request);

        // Estado inicial obligatorio
        office.setStatus(OfficeStatus.ACTIVO);

        Office saved = officeRepository.save(office);

        return officeMapper.toResponse(saved);
    }

    // 🔹 GET BY ID
    @Override
    public OfficeResponse getOfficeById(UUID id) {

        Office office = officeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consultorio no encontrado"));

        return officeMapper.toResponse(office);
    }

    // 🔹 GET ALL
    @Override
    public List<OfficeResponse> getAllOffices() {

        return officeRepository.findAll()
                .stream()
                .map(officeMapper::toResponse)
                .toList();
    }

    // 🔹 UPDATE
    @Override
    public OfficeResponse updateOffice(UUID id, UpdateOfficeRequest request) {

        Office office = officeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consultorio no encontrado"));

        // 🔥 Usa mapper (importante)
        officeMapper.updateEntityFromRequest( request,office);

        Office updated = officeRepository.save(office);

        return officeMapper.toResponse(updated);
    }

    // 🔹 DEACTIVATE (soft delete)
    @Override
    public void deactivateOffice(UUID id) {

        Office office = officeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Consultorio no encontrado"));

        if (office.getStatus() == OfficeStatus.INACTIVO) {
            throw new BusinessException("El consultorio ya está inactivo");
        }

        office.setStatus(OfficeStatus.INACTIVO);

        officeRepository.save(office);
    }
}