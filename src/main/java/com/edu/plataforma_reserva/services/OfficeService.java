package com.edu.plataforma_reserva.services;

import com.edu.plataforma_reserva.dtos.CreateOfficeRequest;
import com.edu.plataforma_reserva.dtos.OfficeResponse;
import com.edu.plataforma_reserva.dtos.UpdateOfficeRequest;

import java.util.List;
import java.util.UUID;

public interface OfficeService {

    OfficeResponse createOffice(CreateOfficeRequest request);

    OfficeResponse getOfficeById(UUID id);

    List<OfficeResponse> getAllOffices();

    OfficeResponse updateOffice(UUID id, UpdateOfficeRequest request);

    void deactivateOffice(UUID id);
}