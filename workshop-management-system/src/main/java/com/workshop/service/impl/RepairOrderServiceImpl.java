package com.workshop.service.impl;

import com.workshop.dto.RepairOrderDTO;
import com.workshop.exception.CarNotFoundException;
import com.workshop.exception.RepairOrderNotFoundException;
import com.workshop.mapper.RepairOrderMapper;
import com.workshop.model.Car;
import com.workshop.model.RepairOrder;
import com.workshop.model.RepairStatus;
import com.workshop.repository.CarRepository;
import com.workshop.repository.RepairOrderRepository;
import com.workshop.service.RepairOrderService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class RepairOrderServiceImpl implements RepairOrderService {

    private final RepairOrderRepository repository;
    private final CarRepository carRepository;
    private final RepairOrderMapper mapper;

    public RepairOrderServiceImpl(RepairOrderRepository repository,
                                  CarRepository carRepository,
                                  RepairOrderMapper mapper) {
        this.repository = repository;
        this.carRepository = carRepository;
        this.mapper = mapper;
    }

    @Override
    public RepairOrderDTO create(Long carId, RepairOrderDTO dto) {
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CarNotFoundException(carId));

        RepairOrder order = mapper.toEntity(dto);
        order.setCar(car);
        order.setCreationDate(LocalDate.now());
        order.setStatus(RepairStatus.OPEN);

        return mapper.toDTO(repository.save(order));
    }

    @Override
    public RepairOrderDTO getById(Long id) {
        RepairOrder order = repository.findById(id)
                .orElseThrow(() -> new RepairOrderNotFoundException(id));
        return mapper.toDTO(order);
    }

    @Override
    public List<RepairOrderDTO> getByCarId(Long carId) {
        if (!carRepository.existsById(carId)) {
            throw new CarNotFoundException(carId);
        }

        return repository.findAll()
                .stream()
                .filter(o -> o.getCar().getId().equals(carId))
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public List<RepairOrderDTO> getAll() {
        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public RepairOrderDTO update(Long id, RepairOrderDTO dto) {
        RepairOrder order = repository.findById(id)
                .orElseThrow(() -> new RepairOrderNotFoundException(id));

        order.setDescription(dto.getDescription());
        order.setCost(dto.getCost());
        order.setStatus(dto.getStatus());

        return mapper.toDTO(repository.save(order));
    }

    @Override
    public RepairOrderDTO closeOrder(Long id) {
        RepairOrder order = repository.findById(id)
                .orElseThrow(() -> new RepairOrderNotFoundException(id));

        order.setStatus(RepairStatus.FINISHED);
        order.setClosingDate(LocalDate.now());

        return mapper.toDTO(repository.save(order));
    }
}

