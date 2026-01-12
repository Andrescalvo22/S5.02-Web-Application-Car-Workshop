package com.workshop.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class RepairNoteResponse {
    private Long id;
    private String text;
    private String author;
    private LocalDateTime createdAt;
}

