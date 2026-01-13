package com.workshop.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepairOrderDetailsDTO {
    private Long id;
    private String description;
    private LocalDate creationDate;
    private LocalDate closingDate;
    private String status;
    private double cost;

    //car
    private Long carId;
    private String plateNumber;
    private String brand;
    private String model;
    private Integer year;
    private String carStatus;

    // customer
    private Long customerId;
    private String customerName;
    private String customerEmail;
}

