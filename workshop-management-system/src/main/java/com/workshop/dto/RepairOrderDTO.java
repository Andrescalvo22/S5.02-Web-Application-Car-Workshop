package com.workshop.dto;

import lombok.Data;
import com.workshop.model.RepairStatus;

import java.time.LocalDate;

@Data
public class RepairOrderDTO {
    private Long id;
    private String description;
    private LocalDate creationDate;
    private LocalDate closingDate;
    private RepairStatus status;
    private double cost;
    private Long carId;
}
