package com.workshop.service;

import com.workshop.dto.CustomerDTO;

import java.util.List;

public interface CustomerService {
    CustomerDTO create(CustomerDTO dto);

    List<CustomerDTO> getAll();

    CustomerDTO getById(Long id);

    CustomerDTO update(Long id, CustomerDTO dto);

    void delete(Long id);
}

