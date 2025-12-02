package com.workshop.dto;

import lombok.Data;

@Data
public class RepairTaskDTO {
    private Long id;
    private String task;
    private Double cost;
    private Long repairOrderId;
}
