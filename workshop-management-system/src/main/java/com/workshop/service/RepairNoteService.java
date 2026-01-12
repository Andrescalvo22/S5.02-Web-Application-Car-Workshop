package com.workshop.service;

import com.workshop.dto.RepairNoteRequest;
import com.workshop.dto.RepairNoteResponse;

import java.util.List;

public interface RepairNoteService {
    List<RepairNoteResponse> getNotes(Long repairOrderId);
    RepairNoteResponse addNote(Long repairOrderId, RepairNoteRequest request);
}

