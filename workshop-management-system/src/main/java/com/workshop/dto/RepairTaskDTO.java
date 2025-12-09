package com.workshop.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RepairTaskDTO {
    private Long id;
    private String task;
    private Double cost;
    private Long repairOrderId;
}
