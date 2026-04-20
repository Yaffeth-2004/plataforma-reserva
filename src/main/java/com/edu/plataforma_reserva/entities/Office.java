package com.edu.plataforma_reserva.entities;

import com.edu.plataforma_reserva.enums.OfficeStatus;
import jakarta.persistence.*;
import lombok.*;


import java.util.UUID;

@Entity
@Table(name = "offices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Office {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    private String location;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private OfficeStatus status;


}
