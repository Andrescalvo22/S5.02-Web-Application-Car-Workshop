package com.workshop.service;

import com.workshop.dto.CarDTO;

import java.util.List;

public interface CarService {
    CarDTO createForCustomer(Long customerId, CarDTO dto);

    List<CarDTO> getByCustomer(Long customerId);

    CarDTO getById(Long id);

    CarDTO update(Long id, CarDTO dto);

    CarDTO updateStatus(Long id, String newStatus);

    List<CarDTO> getAll();

    void delete(Long id);
}
