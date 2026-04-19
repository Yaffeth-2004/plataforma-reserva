package com.edu.plataforma_reserva.repositories;
/*
import com.edu.plataforma_reserva.entities.Office;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OfficeRepositoryIT extends AbstractRepositoryIT {

    @Autowired
    private OfficeRepository officeRepository;

    @Test
    void shouldSaveOffice() {

        // =========================
        // ARRANGE
        // Creamos un consultorio
        // =========================
        Office office = Office.builder()
                .name("Consultorio 101")
                .build();

        // =========================
        // ACT
        // Guardamos en la BD real
        // =========================
        Office saved = officeRepository.save(office);

        // =========================
        // ASSERT
        // =========================
        assertThat(saved.getId()).isNotNull();
    }

    @Test
    void shouldFindOfficeById() {

        // ARRANGE
        Office office = officeRepository.save(
                Office.builder()
                        .name("Consultorio 102")
                        .build()
        );

        // ACT
        Office found = officeRepository.findById(office.getId()).orElse(null);

        // ASSERT
        assertThat(found).isNotNull();
        assertThat(found.getName()).isEqualTo("Consultorio 102");
    }

    @Test
    void shouldFindAllOffices() {

        // ARRANGE
        officeRepository.save(
                Office.builder().name("A").build()
        );

        officeRepository.save(
                Office.builder().name("B").build()
        );

        // ACT
        List<Office> offices = officeRepository.findAll();

        // ASSERT
        assertThat(offices).hasSize(2);
    }
}

 */