package com.workshop.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RepairTaskDTO {
    private Long id;
    private String task;
    private Double cost;
    private Long repairOrderId;
}
