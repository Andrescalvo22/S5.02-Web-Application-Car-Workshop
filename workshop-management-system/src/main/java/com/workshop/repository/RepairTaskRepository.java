package com.workshop.repository;

import com.workshop.model.RepairTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepairTaskRepository extends JpaRepository<RepairTask, Long> {
    List<RepairTask> findByRepairOrderId(Long repairOrderId);

}
