package com.workshop.service;

import com.workshop.dto.RepairOrderDTO;

import java.util.List;

public interface RepairOrderService {

    RepairOrderDTO create(Long carId, RepairOrderDTO dto);

    RepairOrderDTO getById(Long id);

    List<RepairOrderDTO> getByCustomerId(Long customerId);

    List<RepairOrderDTO> getByCarId(Long carId);

    List<RepairOrderDTO> getAll();

    RepairOrderDTO update(Long id, RepairOrderDTO dto);

    RepairOrderDTO closeOrder(Long id);

    List<RepairOrderDTO> getMyOrders();

}

