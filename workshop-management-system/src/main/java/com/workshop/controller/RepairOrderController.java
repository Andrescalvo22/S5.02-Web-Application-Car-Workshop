package com.workshop.controller;

import com.workshop.dto.RepairOrderDTO;
import com.workshop.service.RepairOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/repair-orders")
@RequiredArgsConstructor
@Tag(name = "Repair Orders", description = "Operations for managing car repair orders")
public class RepairOrderController {

    private final RepairOrderService service;

    // =====================
    // CREATE
    // =====================
    @Operation(summary = "Create a repair order for a specific car")
    @PostMapping("/car/{carId}")
    public ResponseEntity<RepairOrderDTO> create(@PathVariable Long carId,
                                                 @Valid @RequestBody RepairOrderDTO dto) {
        RepairOrderDTO created = service.create(carId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // =====================
    // READ
    // =====================
    @Operation(summary = "Get all repair orders")
    @GetMapping
    public ResponseEntity<List<RepairOrderDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(summary = "Get repair order by ID")
    @GetMapping("/{id}")
    public ResponseEntity<RepairOrderDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(summary = "Get all repair orders for a specific car")
    @GetMapping("/car/{carId}")
    public ResponseEntity<List<RepairOrderDTO>> getByCarId(@PathVariable Long carId) {
        return ResponseEntity.ok(service.getByCarId(carId));
    }

    @Operation(summary = "Update the status of a repair order")
    @PutMapping("/{id}/status")
    public ResponseEntity<RepairOrderDTO> update(@PathVariable Long id,
                                                 @RequestParam RepairOrderDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Delete a repair order")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> closeOrder(@PathVariable Long id) {
        service.closeOrder(id);
        return ResponseEntity.noContent().build();
    }
}

