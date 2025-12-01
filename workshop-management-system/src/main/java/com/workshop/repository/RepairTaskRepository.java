package com.workshop.repository;

import com.workshop.model.RepairTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepairTaskRepository extends JpaRepository<RepairTask, Long> {
}
