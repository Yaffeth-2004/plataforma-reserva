package com.edu.plataforma_reserva.services;


import com.edu.plataforma_reserva.dtos.CreateSpecialtyRequest;
import com.edu.plataforma_reserva.dtos.SpecialtyResponse;
import com.edu.plataforma_reserva.entities.Specialty;
import com.edu.plataforma_reserva.exeptions.BusinessException;
import com.edu.plataforma_reserva.mappers.SpecialtyMapper;
import com.edu.plataforma_reserva.repositories.SpecialtyRepository;
import com.edu.plataforma_reserva.services.SpecialtyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpecialtyServiceImpl implements SpecialtyService {

    private final SpecialtyRepository specialtyRepository;
    private final SpecialtyMapper specialtyMapper;

    // 🔹 Crear especialidad
    @Override
    public SpecialtyResponse createSpecialty(CreateSpecialtyRequest request) {

        // 🔥 Validación importante (evitar duplicados)
        if (specialtyRepository.existsByNameIgnoreCase(request.getName())) {
            throw new BusinessException("La especialidad ya existe");
        }

        Specialty specialty = specialtyMapper.toEntity(request);

        Specialty saved = specialtyRepository.save(specialty);

        return specialtyMapper.toResponse(saved);
    }

    // 🔹 Listar todas las especialidades
    @Override
    public List<SpecialtyResponse> getAllSpecialties() {

        return specialtyRepository.findAll()
                .stream()
                .map(specialtyMapper::toResponse)
                .toList();
    }
}
