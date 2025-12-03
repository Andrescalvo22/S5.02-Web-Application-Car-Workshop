package com.workshop.service;

import com.workshop.dto.RepairTaskDTO;

import java.util.List;

public interface RepairTaskService {

    RepairTaskDTO create(Long repairOrderId, RepairTaskDTO dto);

    RepairTaskDTO getById(Long id);

    List<RepairTaskDTO> getByOrderId(Long orderId);

    List<RepairTaskDTO> getAll();

    RepairTaskDTO update(Long id, RepairTaskDTO dto);

    void delete(Long id);
}

