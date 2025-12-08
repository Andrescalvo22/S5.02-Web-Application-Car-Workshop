package com.workshop.controller;

import com.workshop.dto.CarDTO;
import com.workshop.service.CarService;
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
@RequestMapping("/api/cars")
@RequiredArgsConstructor
@Tag(name = "Cars", description = "Operations related to vehicle management")
public class CarController {

    private final CarService carService;

    @Operation(summary = "Create a new car for a specific customer")
    @PreAuthorize("hasRole('ADMIN') or hasRole('CUSTOMER')")
    @PostMapping("/customer/{customerId}")
    public ResponseEntity<CarDTO> createForCustomer(@PathVariable Long customerId,
                                                    @Valid @RequestBody CarDTO dto) {
        CarDTO created = carService.createForCustomer(customerId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @Operation(summary = "Get all cars in the system")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<CarDTO>> getAll() {
        return ResponseEntity.ok(carService.getAll());
    }

    @Operation(summary = "Get a car by its ID")
    @GetMapping("/{id}")
    public ResponseEntity<CarDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(carService.getById(id));
    }

    @Operation(summary = "Get all cars belonging to a customer")
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<CarDTO>> getByCustomer(@PathVariable Long customerId) {
        return ResponseEntity.ok(carService.getByCustomer(customerId));
    }

    @Operation(summary = "Update all the details of a car")
    @PutMapping("/{id}")
    public ResponseEntity<CarDTO> update(@PathVariable Long id,
                                         @Valid @RequestBody CarDTO dto) {
        return ResponseEntity.ok(carService.update(id, dto));
    }

    @Operation(summary = "Update only the status of a car")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/status")
    public ResponseEntity<CarDTO> updateStatus(@PathVariable Long id,
                                               @RequestParam String status) {
        return ResponseEntity.ok(carService.updateStatus(id, status));
    }

    @Operation(summary = "Delete a car by its ID")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        carService.delete(id);
        return ResponseEntity.noContent().build();
    }
}


