package com.workshop.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepairOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private LocalDate creationDate;
    private LocalDate closingDate;

    @Enumerated(EnumType.STRING)
    private RepairStatus status;

    private double cost;

    @ManyToOne
    @JoinColumn(name = "car_id")
    private Car car;
}
