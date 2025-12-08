package com.workshop.service.impl;

import com.workshop.dto.RepairTaskDTO;
import com.workshop.exception.RepairOrderNotFoundException;
import com.workshop.exception.RepairTaskNotFoundException;
import com.workshop.mapper.RepairTaskMapper;
import com.workshop.model.RepairOrder;
import com.workshop.model.RepairTask;
import com.workshop.model.User;
import com.workshop.repository.RepairOrderRepository;
import com.workshop.repository.RepairTaskRepository;
import com.workshop.repository.UserRepository;
import com.workshop.service.RepairTaskService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepairTaskServiceImpl implements RepairTaskService {

    private final RepairTaskRepository repository;
    private final RepairOrderRepository orderRepository;
    private final RepairTaskMapper mapper;
    private final UserRepository userRepository;

    public RepairTaskServiceImpl(
            RepairTaskRepository repository,
            RepairOrderRepository orderRepository,
            RepairTaskMapper mapper,
            UserRepository userRepository
    ) {
        this.repository = repository;
        this.orderRepository = orderRepository;
        this.mapper = mapper;
        this.userRepository = userRepository;
    }

    private User getAuthenticatedUser() {

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email;

        if (principal instanceof UserDetails userDetails) {
            email = userDetails.getUsername();
        } else {
            email = principal.toString();
        }

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private boolean isAdmin(User user) {
        return user.getRoles().stream().anyMatch(r -> r.name().equals("ROLE_ADMIN"));
    }

    private void validateOwnershipOrAdmin(RepairOrder order, User user) {

        if (order.getCar() == null || order.getCar().getCustomer() == null) {
            throw new RuntimeException("Order does not have a valid customer assigned");
        }

        Long orderCustomerId = order.getCar().getCustomer().getId();

        if (!isAdmin(user) && !orderCustomerId.equals(user.getCustomerId())) {
            throw new RuntimeException("Access denied");
        }
    }

    @Override
    public RepairTaskDTO create(Long repairOrderId, RepairTaskDTO dto) {

        User user = getAuthenticatedUser();
        RepairOrder order = orderRepository.findById(repairOrderId)
                .orElseThrow(() -> new RepairOrderNotFoundException(repairOrderId));

        validateOwnershipOrAdmin(order, user);

        RepairTask task = mapper.toEntity(dto);
        task.setRepairOrder(order);

        return mapper.toDTO(repository.save(task));
    }

    @Override
    public RepairTaskDTO getById(Long id) {

        User user = getAuthenticatedUser();
        RepairTask task = repository.findById(id)
                .orElseThrow(() -> new RepairTaskNotFoundException(id));

        validateOwnershipOrAdmin(task.getRepairOrder(), user);

        return mapper.toDTO(task);
    }

    @Override
    public List<RepairTaskDTO> getByOrderId(Long orderId) {

        User user = getAuthenticatedUser();
        RepairOrder order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RepairOrderNotFoundException(orderId));

        validateOwnershipOrAdmin(order, user);

        List<RepairTask> tasks = repository.findByRepairOrderId(orderId);

        return tasks.stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public List<RepairTaskDTO> getAll() {

        User user = getAuthenticatedUser();

        if (!isAdmin(user)) {
            throw new RuntimeException("Only admins can view all tasks");
        }

        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public RepairTaskDTO update(Long id, RepairTaskDTO dto) {

        User user = getAuthenticatedUser();

        if (!isAdmin(user)) {
            throw new RuntimeException("Only admins can update tasks");
        }

        RepairTask task = repository.findById(id)
                .orElseThrow(() -> new RepairTaskNotFoundException(id));

        task.setTask(dto.getTask());
        task.setCost(dto.getCost());

        return mapper.toDTO(repository.save(task));
    }

    @Override
    public void delete(Long id) {

        User user = getAuthenticatedUser();

        if (!isAdmin(user)) {
            throw new RuntimeException("Only admins can delete tasks");
        }

        if (!repository.existsById(id)) {
            throw new RepairTaskNotFoundException(id);
        }

        repository.deleteById(id);
    }
}
