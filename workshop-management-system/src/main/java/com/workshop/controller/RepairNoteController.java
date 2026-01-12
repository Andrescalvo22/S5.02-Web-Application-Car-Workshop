package com.workshop.controller;

import com.workshop.dto.RepairNoteRequest;
import com.workshop.dto.RepairNoteResponse;
import com.workshop.model.RepairNote;
import com.workshop.model.RepairOrder;
import com.workshop.repository.RepairNoteRepository;
import com.workshop.repository.RepairOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/repair-orders")
@RequiredArgsConstructor
public class RepairNoteController {

    private final RepairOrderRepository repairOrderRepository;
    private final RepairNoteRepository repairNoteRepository;

    @GetMapping("/{id}/notes")
    public ResponseEntity<List<RepairNoteResponse>> getNotes(@PathVariable Long id) {
        List<RepairNoteResponse> notes = repairNoteRepository
                .findByRepairOrderIdOrderByCreatedAtDesc(id)
                .stream()
                .map(n -> RepairNoteResponse.builder()
                        .id(n.getId())
                        .text(n.getText())
                        .author(n.getAuthor())
                        .createdAt(n.getCreatedAt())
                        .build()
                )
                .toList();

        return ResponseEntity.ok(notes);
    }

    @PostMapping("/{id}/notes")
    public ResponseEntity<RepairNoteResponse> addNote(
            @PathVariable Long id,
            @RequestBody RepairNoteRequest request,
            Authentication auth
    ) {
        RepairOrder order = repairOrderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Repair order not found"));

        if (request.getText() == null || request.getText().trim().isEmpty()) {
            throw new RuntimeException("Note text cannot be empty");
        }

        String author = (auth != null && auth.getName() != null) ? auth.getName() : "Admin";

        RepairNote note = RepairNote.builder()
                .text(request.getText().trim())
                .author(author)
                .createdAt(LocalDateTime.now())
                .repairOrder(order)
                .build();

        RepairNote saved = repairNoteRepository.save(note);

        return ResponseEntity.ok(
                RepairNoteResponse.builder()
                        .id(saved.getId())
                        .text(saved.getText())
                        .author(saved.getAuthor())
                        .createdAt(saved.getCreatedAt())
                        .build()
        );
    }
}

