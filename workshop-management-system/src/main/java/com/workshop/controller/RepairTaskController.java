package com.workshop.controller;

import com.workshop.dto.RepairTaskDTO;
import com.workshop.service.RepairTaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
@Tag(name = "Repair Tasks", description = "Operations for managing repair tasks within repair orders")
public class RepairTaskController {

    private final RepairTaskService service;

    @Operation(summary = "Create a new repair task for a specific repair order")
    @PostMapping("/order/{orderId}")
    public ResponseEntity<RepairTaskDTO> create(@PathVariable Long orderId,
                                                @Valid @RequestBody RepairTaskDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(orderId, dto));
    }

    @Operation(summary = "Get all repair tasks")
    @GetMapping
    public ResponseEntity<List<RepairTaskDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(summary = "Get a repair task by ID")
    @GetMapping("/{id}")
    public ResponseEntity<RepairTaskDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(summary = "Get all tasks associated with a repair order")
    @GetMapping("/order/{orderId}")
    public ResponseEntity<List<RepairTaskDTO>> getByOrderId(@PathVariable Long orderId) {
        return ResponseEntity.ok(service.getByOrderId(orderId));
    }

    @Operation(summary = "Update a repair task")
    @PutMapping("/{id}")
    public ResponseEntity<RepairTaskDTO> update(@PathVariable Long id,
                                                @Valid @RequestBody RepairTaskDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Delete a repair task")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
