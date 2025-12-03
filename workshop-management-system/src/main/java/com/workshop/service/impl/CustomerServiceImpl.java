package com.workshop.service.impl;

import com.workshop.dto.CustomerDTO;
import com.workshop.exception.CustomerNotFoundException;
import com.workshop.mapper.CustomerMapper;
import com.workshop.model.Customer;
import com.workshop.repository.CustomerRepository;
import com.workshop.service.CustomerService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    public CustomerServiceImpl(CustomerRepository repository, CustomerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public CustomerDTO create(CustomerDTO dto) {
        Customer customer = mapper.toEntity(dto);
        return mapper.toDTO(repository.save(customer));
    }

    @Override
    public List<CustomerDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public CustomerDTO getById(Long id) {
        Customer customer = repository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));
        return mapper.toDTO(customer);
    }

    @Override
    public CustomerDTO update(Long id, CustomerDTO dto) {
        Customer existing = repository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        existing.setName(dto.getName());
        existing.setLastName(dto.getLastName());
        existing.setTelephone(dto.getTelephone());
        existing.setEmail(dto.getEmail());

        return mapper.toDTO(repository.save(existing));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new CustomerNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
