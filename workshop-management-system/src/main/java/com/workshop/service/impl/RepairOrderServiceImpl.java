package com.workshop.service.impl;

import com.workshop.dto.RepairOrderDTO;
import com.workshop.exception.CarNotFoundException;
import com.workshop.exception.RepairOrderNotFoundException;
import com.workshop.exception.UserNotFoundException;
import com.workshop.mapper.RepairOrderMapper;
import com.workshop.model.Car;
import com.workshop.model.RepairOrder;
import com.workshop.model.RepairStatus;
import com.workshop.model.User;
import com.workshop.repository.CarRepository;
import com.workshop.repository.RepairOrderRepository;
import com.workshop.repository.UserRepository;
import com.workshop.service.RepairOrderService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDate;
import java.util.List;

@Service
public class RepairOrderServiceImpl implements RepairOrderService {

    private final RepairOrderRepository repository;
    private final CarRepository carRepository;
    private final RepairOrderMapper mapper;
    private final UserRepository userRepository;

    public RepairOrderServiceImpl(RepairOrderRepository repository,
                                  CarRepository carRepository,
                                  RepairOrderMapper mapper,
                                  UserRepository userRepository) {
        this.repository = repository;
        this.carRepository = carRepository;
        this.mapper = mapper;
        this.userRepository = userRepository;
    }

    private User getAuthenticatedUser() {
        String email = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private boolean isAdmin(User user) {
        return user.getRoles().stream().anyMatch(r -> r.name().equals("ROLE_ADMIN"));
    }

    private void checkOwnershipOrAdmin(Car car, User user, String message) throws AccessDeniedException {
        if (!isAdmin(user) && !car.getCustomer().getId().equals(user.getCustomerId())) {
            throw new AccessDeniedException(message);
        }
    }

    @Override
    public List<RepairOrderDTO> getMyOrders() {

        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        if (user.getCustomerId() == null) {
            return List.of();
        }

        List<RepairOrder> orders = repository
                .findByCar_Customer_Id(user.getCustomerId());

        return orders.stream()
                .map(mapper::toDTO)
                .toList();
    }


    @Override
    public RepairOrderDTO create(Long carId, RepairOrderDTO dto) {

        User user = getAuthenticatedUser();
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CarNotFoundException(carId));

        checkOwnershipOrAdmin(car, user, "You are not allowed to create repair orders for this car");

        RepairOrder order = mapper.toEntity(dto);
        order.setCar(car);
        order.setCreationDate(LocalDate.now());
        order.setStatus(RepairStatus.PENDING);

        return mapper.toDTO(repository.save(order));
    }

    @Override
    public RepairOrderDTO getById(Long id) {

        User user = getAuthenticatedUser();

        RepairOrder order = repository.findById(id)
                .orElseThrow(() -> new RepairOrderNotFoundException(id));

        checkOwnershipOrAdmin(order.getCar(), user, "You cannot access this repair order");

        return mapper.toDTO(order);
    }

    @Override
    public List<RepairOrderDTO> getByCustomerId(Long customerId) {
        return repository.findByCarCustomerId(customerId)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public List<RepairOrderDTO> getByCarId(Long carId) {

        User user = getAuthenticatedUser();
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new CarNotFoundException(carId));

        checkOwnershipOrAdmin(car, user, "You cannot access this car's repair orders");

        return repository.findAll()
                .stream()
                .filter(o -> o.getCar().getId().equals(carId))
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public List<RepairOrderDTO> getAll() {

        User user = getAuthenticatedUser();
        if (!isAdmin(user)) {
            throw new RuntimeException("Only ADMIN can view all repair orders");
        }

        return repository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }

    @Override
    public RepairOrderDTO update(Long id, RepairOrderDTO dto) {

        User user = getAuthenticatedUser();

        RepairOrder order = repository.findById(id)
                .orElseThrow(() -> new RepairOrderNotFoundException(id));

        checkOwnershipOrAdmin(order.getCar(), user, "You cannot update this repair order");

        order.setDescription(dto.getDescription());
        order.setCost(dto.getCost());
        order.setStatus(dto.getStatus());

        return mapper.toDTO(repository.save(order));
    }

    @Override
    public RepairOrderDTO closeOrder(Long id) {

        User user = getAuthenticatedUser();

        RepairOrder order = repository.findById(id)
                .orElseThrow(() -> new RepairOrderNotFoundException(id));

        checkOwnershipOrAdmin(order.getCar(), user, "You cannot close this repair order");

        order.setStatus(RepairStatus.CLOSED);
        order.setClosingDate(LocalDate.now());

        return mapper.toDTO(repository.save(order));
    }
}

