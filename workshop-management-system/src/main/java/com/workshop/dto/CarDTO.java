package com.workshop.dto;

import com.workshop.model.CarStatus;
import lombok.Data;

@Data
public class CarDTO {
    private Long id;
    private String plateNumber;
    private String brand;
    private String model;
    private Integer year;
    private CarStatus status;
    private Long customerId;
}
