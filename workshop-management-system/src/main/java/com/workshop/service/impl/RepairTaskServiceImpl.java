package com.workshop.service.impl;

import com.workshop.dto.RepairTaskDTO;
import com.workshop.exception.RepairOrderNotFoundException;
import com.workshop.exception.RepairTaskNotFoundException;
import com.workshop.mapper.RepairTaskMapper;
import com.workshop.model.RepairOrder;
import com.workshop.model.RepairTask;
import com.workshop.repository.RepairOrderRepository;
import com.workshop.repository.RepairTaskRepository;
import com.workshop.service.RepairTaskService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RepairTaskServiceImpl implements RepairTaskService {

    private final RepairTaskRepository repository;
    private final RepairOrderRepository orderRepository;
    private final RepairTaskMapper mapper;

    public RepairTaskServiceImpl(RepairTaskRepository repository,
                                 RepairOrderRepository orderRepository,
                                 RepairTaskMapper mapper) {
        this.repository = repository;
        this.orderRepository = orderRepository;
        this.mapper = mapper;
    }

    @Override
    public RepairTaskDTO create(Long repairOrderId, RepairTaskDTO dto) {
        RepairOrder order = orderRepository.findById(repairOrderId)
                .orElseThrow(() -> new RepairOrderNotFoundException(repairOrderId));

        RepairTask task = mapper.toEntity(dto);
        task.setRepairOrder(order);

        RepairTask saved = repository.save(task);
        return mapper.toDTO(saved);
    }

    @Override
    public RepairTaskDTO getById(Long id) {
        RepairTask task = repository.findById(id)
                .orElseThrow(() -> new RepairTaskNotFoundException(id));

        return mapper.toDTO(task);
    }

    @Override
    public List<RepairTaskDTO> getByOrderId(Long orderId) {
        if (!orderRepository.existsById(orderId)) {
            throw new RepairOrderNotFoundException(orderId);
        }

        return repository.findAll().stream()
                .filter(t -> t.getRepairOrder().getId().equals(orderId))
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public List<RepairTaskDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public RepairTaskDTO update(Long id, RepairTaskDTO dto) {
        RepairTask task = repository.findById(id)
                .orElseThrow(() -> new RepairTaskNotFoundException(id));

        task.setTask(dto.getTask());
        task.setCost(dto.getCost());

        return mapper.toDTO(repository.save(task));
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RepairTaskNotFoundException(id);
        }
        repository.deleteById(id);
    }
}
