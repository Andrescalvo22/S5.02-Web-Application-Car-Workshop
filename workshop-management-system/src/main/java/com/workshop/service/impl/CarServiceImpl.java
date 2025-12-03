package com.workshop.service.impl;

import com.workshop.dto.CarDTO;
import com.workshop.exception.CarNotFoundException;
import com.workshop.exception.CustomerNotFoundException;
import com.workshop.exception.InvalidStatusException;
import com.workshop.mapper.CarMapper;
import com.workshop.model.Car;
import com.workshop.model.CarStatus;
import com.workshop.model.Customer;
import com.workshop.repository.CarRepository;
import com.workshop.repository.CustomerRepository;
import com.workshop.service.CarService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarServiceImpl implements CarService {

    private final CarRepository carRepository;
    private final CustomerRepository customerRepository;
    private final CarMapper mapper;

    public CarServiceImpl(CarRepository carRepository,
                          CustomerRepository customerRepository,
                          CarMapper mapper) {
        this.carRepository = carRepository;
        this.customerRepository = customerRepository;
        this.mapper = mapper;
    }

    @Override
    public CarDTO createForCustomer(Long customerId, CarDTO dto) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new CustomerNotFoundException(customerId));

        Car car = mapper.toEntity(dto);
        car.setCustomer(customer);
        car.setStatus(CarStatus.PENDING_DIAGNOSTIC);

        return mapper.toDTO(carRepository.save(car));
    }

    @Override
    public List<CarDTO> getByCustomer(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new CustomerNotFoundException(customerId);
        }

        return carRepository.findAll()
                .stream()
                .filter(c -> c.getCustomer().getId().equals(customerId))
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public CarDTO getById(Long id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(id));
        return mapper.toDTO(car);
    }

    @Override
    public CarDTO update(Long id, CarDTO dto) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(id));

        car.setBrand(dto.getBrand());
        car.setModel(dto.getModel());
        car.setPlateNumber(dto.getPlateNumber());
        car.setYear(dto.getYear());

        return mapper.toDTO(carRepository.save(car));
    }

    @Override
    public CarDTO updateStatus(Long id, String newStatus) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarNotFoundException(id));

        try {
            CarStatus status = CarStatus.valueOf(newStatus.toUpperCase());
            car.setStatus(status);
        } catch (IllegalArgumentException e) {
            throw new InvalidStatusException(newStatus);
        }

        return mapper.toDTO(carRepository.save(car));
    }

    @Override
    public List<CarDTO> getAll() {
        return carRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public void delete(Long id) {
        if (!carRepository.existsById(id)) {
            throw new CarNotFoundException(id);
        }
        carRepository.deleteById(id);
    }


}

