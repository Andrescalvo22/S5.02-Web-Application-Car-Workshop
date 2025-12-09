package com.workshop.service;

import com.workshop.dto.RepairOrderDTO;
import com.workshop.exception.CarNotFoundException;
import com.workshop.exception.RepairOrderNotFoundException;
import com.workshop.mapper.RepairOrderMapper;
import com.workshop.model.*;
import com.workshop.repository.CarRepository;
import com.workshop.repository.RepairOrderRepository;
import com.workshop.repository.UserRepository;
import com.workshop.service.impl.RepairOrderServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RepairOrderServiceImplTest {

    @Mock
    private RepairOrderRepository orderRepository;

    @Mock
    private CarRepository carRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RepairOrderMapper mapper;

    @InjectMocks
    private RepairOrderServiceImpl service;

    private User adminUser;
    private User customerUser;
    private Car car;
    private RepairOrder order;
    private RepairOrderDTO dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Fake authenticated user
        adminUser = User.builder()
                .id(1L)
                .email("admin@test.com")
                .roles(Set.of(Role.ROLE_ADMIN))
                .customerId(10L)
                .build();

        customerUser = User.builder()
                .id(2L)
                .email("user@test.com")
                .roles(Set.of(Role.ROLE_USER))
                .customerId(20L)
                .build();

        Customer customer = new Customer();
        customer.setId(20L);

        car = new Car();
        car.setId(5L);
        car.setCustomer(customer);

        order = new RepairOrder();
        order.setId(100L);
        order.setCar(car);
        order.setStatus(RepairStatus.PENDING);

        dto = new RepairOrderDTO();
        dto.setDescription("Fix brakes");
        dto.setCost(200.0);
        dto.setStatus(RepairStatus.PENDING);
    }

    // -------------------------------
    // Helpers
    // -------------------------------

    private void authenticate(User user) {
        var auth = new UsernamePasswordAuthenticationToken(user.getEmail(), null, List.of());
        SecurityContextHolder.getContext().setAuthentication(auth);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
    }

    // -------------------------------
    // TEST: CREATE
    // -------------------------------

    @Test
    void testCreateRepairOrder_Success() {
        authenticate(customerUser);

        when(carRepository.findById(5L)).thenReturn(Optional.of(car));
        when(mapper.toEntity(dto)).thenReturn(order);
        when(orderRepository.save(order)).thenReturn(order);
        when(mapper.toDTO(order)).thenReturn(dto);

        RepairOrderDTO result = service.create(5L, dto);

        assertNotNull(result);
        assertEquals("Fix brakes", result.getDescription());
        verify(orderRepository).save(order);
    }

    @Test
    void testCreateRepairOrder_CarNotFound() {
        authenticate(customerUser);

        when(carRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(CarNotFoundException.class, () -> service.create(5L, dto));
    }

    // -------------------------------
    // TEST: GET BY ID
    // -------------------------------

    @Test
    void testGetById_Success() {
        authenticate(customerUser);

        when(orderRepository.findById(100L)).thenReturn(Optional.of(order));
        when(mapper.toDTO(order)).thenReturn(dto);

        RepairOrderDTO result = service.getById(100L);

        assertNotNull(result);
        verify(orderRepository).findById(100L);
    }

    @Test
    void testGetById_NotFound() {
        authenticate(customerUser);

        when(orderRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(RepairOrderNotFoundException.class, () -> service.getById(100L));
    }

    // -------------------------------
    // TEST: GET BY CAR ID
    // -------------------------------

    @Test
    void testGetByCarId_Success() {
        authenticate(customerUser);

        when(carRepository.findById(5L)).thenReturn(Optional.of(car));
        when(orderRepository.findAll()).thenReturn(List.of(order));
        when(mapper.toDTO(order)).thenReturn(dto);

        List<RepairOrderDTO> result = service.getByCarId(5L);

        assertEquals(1, result.size());
    }

    @Test
    void testGetByCarId_CarNotFound() {
        authenticate(customerUser);

        when(carRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(CarNotFoundException.class, () -> service.getByCarId(5L));
    }

    // -------------------------------
    // TEST: GET ALL (ADMIN ONLY)
    // -------------------------------

    @Test
    void testGetAll_AdminSuccess() {
        authenticate(adminUser);

        when(orderRepository.findAll()).thenReturn(List.of(order));
        when(mapper.toDTO(order)).thenReturn(dto);

        List<RepairOrderDTO> result = service.getAll();

        assertEquals(1, result.size());
    }

    @Test
    void testGetAll_NotAdmin() {
        authenticate(customerUser);

        assertThrows(RuntimeException.class, () -> service.getAll());
    }

    // -------------------------------
    // TEST: UPDATE
    // -------------------------------

    @Test
    void testUpdate_Success() {
        authenticate(customerUser);

        when(orderRepository.findById(100L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(mapper.toDTO(order)).thenReturn(dto);

        RepairOrderDTO result = service.update(100L, dto);

        assertNotNull(result);
    }

    @Test
    void testUpdate_NotFound() {
        authenticate(customerUser);

        when(orderRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(RepairOrderNotFoundException.class, () -> service.update(100L, dto));
    }

    // -------------------------------
    // TEST: CLOSE ORDER
    // -------------------------------

    @Test
    void testCloseOrder_Success() {
        authenticate(customerUser);

        when(orderRepository.findById(100L)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(mapper.toDTO(order)).thenReturn(dto);

        RepairOrderDTO result = service.closeOrder(100L);

        assertNotNull(result);
        assertEquals(RepairStatus.CLOSED, order.getStatus());
    }

    @Test
    void testCloseOrder_NotFound() {
        authenticate(customerUser);

        when(orderRepository.findById(100L)).thenReturn(Optional.empty());

        assertThrows(RepairOrderNotFoundException.class, () -> service.closeOrder(100L));
    }
}
