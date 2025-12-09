package com.workshop.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String plateNumber;
    private String brand;
    private String model;
    private Integer year;

    @Enumerated(EnumType.STRING)
    private CarStatus status;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
