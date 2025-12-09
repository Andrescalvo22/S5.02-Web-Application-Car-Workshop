package com.workshop.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepairTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String task;
    private Double cost;

    @ManyToOne
    @JoinColumn(name = "repair_order_id")
    private RepairOrder repairOrder;
}

