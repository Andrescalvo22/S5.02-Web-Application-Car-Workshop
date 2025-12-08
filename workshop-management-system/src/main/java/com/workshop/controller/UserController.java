package com.workshop.controller;

import com.workshop.dto.UserDTO;
import com.workshop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User management operations")
public class UserController {

    private final UserService service;

    @Operation(summary = "Get authenticated user's profile")
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getMe() {
        return ResponseEntity.ok(service.getMe());
    }

    @Operation(summary = "Update authenticated user's profile")
    @PutMapping("/me")
    public ResponseEntity<UserDTO> updateMe(@RequestBody UserDTO dto) {
        return ResponseEntity.ok(service.updateMe(dto));
    }

    @Operation(summary = "Get all users (ADMIN only)")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<UserDTO>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    @Operation(summary = "Get user by ID (ADMIN only)")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @Operation(summary = "Update a user (ADMIN only)")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> update(@PathVariable Long id, @RequestBody UserDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    @Operation(summary = "Delete a user (ADMIN only)")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}

