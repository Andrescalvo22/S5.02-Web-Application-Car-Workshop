package com.workshop.service.impl;

import com.workshop.dto.CustomerDTO;
import com.workshop.exception.CustomerNotFoundException;
import com.workshop.mapper.CustomerMapper;
import com.workshop.model.Customer;
import com.workshop.model.User;
import com.workshop.repository.CustomerRepository;
import com.workshop.repository.UserRepository;
import com.workshop.service.CustomerService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;
    private final UserRepository userRepository;

    public CustomerServiceImpl(CustomerRepository repository,
                               CustomerMapper mapper,
                               UserRepository userRepository) {
        this.repository = repository;
        this.mapper = mapper;
        this.userRepository = userRepository;
    }

    private User getAuthenticatedUser() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Authenticated user not found"));
    }

    private boolean isAdmin(User user) {
        return user.getRoles().stream().anyMatch(r -> r.name().equals("ROLE_ADMIN"));
    }

    @Override
    public CustomerDTO create(CustomerDTO dto) {
        User user = getAuthenticatedUser();

        if (!isAdmin(user)) {
            throw new RuntimeException("Only ADMIN can create customers");
        }

        Customer customer = mapper.toEntity(dto);

        customer.setId(null);

        Customer saved = repository.save(customer);

        return mapper.toDTO(saved);
    }

    @Override
    public List<CustomerDTO> getAll() {
        User user = getAuthenticatedUser();

        if (!isAdmin(user)) {
            throw new RuntimeException("Only ADMIN can see all customers");
        }

        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public CustomerDTO getById(Long id) {
        User user = getAuthenticatedUser();

        Customer customer = repository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        if (!isAdmin(user) && !customer.getId().equals(user.getCustomerId())) {
            throw new RuntimeException("You cannot view this customer record");
        }

        return mapper.toDTO(customer);
    }

    @Override
    public CustomerDTO update(Long id, CustomerDTO dto) {
        User user = getAuthenticatedUser();

        Customer existing = repository.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        if (!isAdmin(user) && !existing.getId().equals(user.getCustomerId())) {
            throw new RuntimeException("You cannot modify this customer record");
        }

        existing.setName(dto.getName());
        existing.setLastName(dto.getLastName());
        existing.setTelephone(dto.getTelephone());
        existing.setEmail(dto.getEmail());

        return mapper.toDTO(repository.save(existing));
    }

    @Override
    public void delete(Long id) {
        User user = getAuthenticatedUser();

        if (!isAdmin(user)) {
            throw new RuntimeException("Only ADMIN can delete customers");
        }

        if (!repository.existsById(id)) {
            throw new CustomerNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
