package com.workshop.controller;

import com.workshop.dto.RepairOrderDTO;
import com.workshop.service.RepairOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repair-orders")
@RequiredArgsConstructor
@Tag(name = "Repair Orders", description = "Operations for managing car repair orders")
public class RepairOrderController {

    private final RepairOrderService service;

    @Operation(summary = "Create a repair order for a specific car")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PostMapping("/car/{carId}")
    public ResponseEntity<RepairOrderDTO> create(
            @PathVariable Long carId,
            @Valid @RequestBody RepairOrderDTO dto
    ) {
        RepairOrderDTO created = service.create(carId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Get all repair orders")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<RepairOrderDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(summary = "Get repair order by ID")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/{id}")
    public ResponseEntity<RepairOrderDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(summary = "Get repair orders of authenticated user")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/my")
    public ResponseEntity<List<RepairOrderDTO>> getMyOrders() {
        return ResponseEntity.ok(service.getMyOrders());
    }

    @Operation(summary = "Get all repair orders for a specific car")
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @GetMapping("/car/{carId}")
    public ResponseEntity<List<RepairOrderDTO>> getByCarId(@PathVariable Long carId) {
        return ResponseEntity.ok(service.getByCarId(carId));
    }

    @Operation(summary = "Update a repair order")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<RepairOrderDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody RepairOrderDTO dto
    ) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Close a repair order (status = CLOSED)")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/close")
    public ResponseEntity<RepairOrderDTO> closeOrder(@PathVariable Long id) {
        return ResponseEntity.ok(service.closeOrder(id));
    }
}