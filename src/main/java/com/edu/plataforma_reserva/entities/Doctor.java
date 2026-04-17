package com.edu.plataforma_reserva.entities;

import com.edu.plataforma_reserva.enums.DoctorStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "specialty_id" , referencedColumnName = "id", nullable = false)
    private Specialty specialty;

    @Enumerated(EnumType.STRING)
    private DoctorStatus status;


}