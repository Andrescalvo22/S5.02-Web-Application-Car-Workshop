package com.workshop.controller;

import com.workshop.dto.RepairNoteRequest;
import com.workshop.dto.RepairNoteResponse;
import com.workshop.service.RepairNoteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repair-orders")
@RequiredArgsConstructor
public class RepairNoteController {

    private final RepairNoteService repairNoteService;

    @GetMapping("/{id}/notes")
    public ResponseEntity<List<RepairNoteResponse>> getNotes(@PathVariable Long id) {
        return ResponseEntity.ok(repairNoteService.getNotes(id));
    }

    @PostMapping("/{id}/notes")
    public ResponseEntity<RepairNoteResponse> addNote(
            @PathVariable Long id,
            @RequestBody RepairNoteRequest request
    ) {
        return ResponseEntity.ok(repairNoteService.addNote(id, request));
    }
}


