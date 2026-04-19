package com.edu.plataforma_reserva.services;

import com.edu.plataforma_reserva.dtos.CreateSpecialtyRequest;
import com.edu.plataforma_reserva.dtos.SpecialtyResponse;

import java.util.List;

public interface SpecialtyService {

    SpecialtyResponse createSpecialty(CreateSpecialtyRequest request);

    List<SpecialtyResponse> getAllSpecialties();
}