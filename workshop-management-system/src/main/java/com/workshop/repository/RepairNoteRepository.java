package com.workshop.repository;

import com.workshop.model.RepairNote;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepairNoteRepository extends JpaRepository<RepairNote, Long> {
    List<RepairNote> findByRepairOrderIdOrderByCreatedAtDesc(Long repairOrderId);
}

