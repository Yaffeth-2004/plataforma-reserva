package com.edu.plataforma_reserva.entities;



import com.edu.plataforma_reserva.enums.PatientStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "patients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;
    @Column(unique = true)
    private String identity;

    private String phone;

    @Enumerated(EnumType.STRING)
    private PatientStatus status;
}
