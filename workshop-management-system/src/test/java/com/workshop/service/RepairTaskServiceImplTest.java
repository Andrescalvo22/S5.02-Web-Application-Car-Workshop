package com.workshop.service;

import com.workshop.dto.RepairTaskDTO;
import com.workshop.exception.RepairOrderNotFoundException;
import com.workshop.exception.RepairTaskNotFoundException;
import com.workshop.mapper.RepairTaskMapper;
import com.workshop.model.Car;
import com.workshop.model.Customer;
import com.workshop.model.RepairOrder;
import com.workshop.model.RepairTask;
import com.workshop.model.Role;
import com.workshop.model.User;
import com.workshop.repository.RepairOrderRepository;
import com.workshop.repository.RepairTaskRepository;
import com.workshop.repository.UserRepository;
import com.workshop.service.impl.RepairTaskServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class RepairTaskServiceImplTest {

    @Mock
    private RepairTaskRepository repository;

    @Mock
    private RepairOrderRepository orderRepository;

    @Mock
    private RepairTaskMapper mapper;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RepairTaskServiceImpl service;

    private User userCustomer;
    private User userAdmin;
    private RepairOrder order;
    private RepairTask task;
    private RepairTaskDTO taskDTO;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        Customer customer = Customer.builder()
                .id(1L)
                .name("John")
                .lastName("Doe")
                .build();

        Car car = Car.builder()
                .id(10L)
                .customer(customer)
                .build();

        order = RepairOrder.builder()
                .id(5L)
                .car(car)
                .build();

        task = RepairTask.builder()
                .id(100L)
                .task("Fix brakes")
                .cost(200.0)
                .repairOrder(order)
                .build();

        taskDTO = RepairTaskDTO.builder()
                .id(100L)
                .task("Fix brakes")
                .cost(200.0)
                .build();

        userCustomer = User.builder()
                .email("customer@email.com")
                .customerId(1L)
                .roles(Set.of(Role.ROLE_USER))
                .build();

        userAdmin = User.builder()
                .email("admin@email.com")
                .roles(Set.of(Role.ROLE_ADMIN))
                .build();
    }

    private void mockAuthenticatedUser(User user) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user.getEmail(), null, Collections.emptyList())
        );

        when(userRepository.findByEmail(user.getEmail()))
                .thenReturn(Optional.of(user));
    }

    @Test
    void create_ShouldCreateTask_WhenUserOwnsOrder() {

        mockAuthenticatedUser(userCustomer);

        when(orderRepository.findById(5L)).thenReturn(Optional.of(order));
        when(mapper.toEntity(any())).thenReturn(task);
        when(repository.save(any())).thenReturn(task);
        when(mapper.toDTO(task)).thenReturn(taskDTO);

        RepairTaskDTO result = service.create(5L, taskDTO);

        assertThat(result).isNotNull();
        verify(repository).save(task);
    }

    @Test
    void create_ShouldThrow_WhenOrderNotFound() {
        mockAuthenticatedUser(userCustomer);

        when(orderRepository.findById(5L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.create(5L, taskDTO))
                .isInstanceOf(RepairOrderNotFoundException.class);
    }

    @Test
    void getById_ShouldReturnTask_WhenUserHasAccess() {

        mockAuthenticatedUser(userCustomer);

        when(repository.findById(100L)).thenReturn(Optional.of(task));
        when(mapper.toDTO(task)).thenReturn(taskDTO);

        RepairTaskDTO result = service.getById(100L);

        assertThat(result).isNotNull();
    }

    @Test
    void getById_ShouldThrow_WhenTaskNotFound() {

        mockAuthenticatedUser(userCustomer);

        when(repository.findById(100L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getById(100L))
                .isInstanceOf(RepairTaskNotFoundException.class);
    }

    @Test
    void getByOrderId_ShouldReturnTasks_WhenUserOwnsOrder() {

        mockAuthenticatedUser(userCustomer);

        when(orderRepository.findById(5L)).thenReturn(Optional.of(order));
        when(repository.findByRepairOrderId(5L)).thenReturn(List.of(task));
        when(mapper.toDTO(task)).thenReturn(taskDTO);

        List<RepairTaskDTO> result = service.getByOrderId(5L);

        assertThat(result).hasSize(1);
    }

    @Test
    void getByOrderId_ShouldThrow_WhenOrderNotFound() {

        mockAuthenticatedUser(userCustomer);

        when(orderRepository.findById(5L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.getByOrderId(5L))
                .isInstanceOf(RepairOrderNotFoundException.class);
    }

    @Test
    void getAll_ShouldReturnList_WhenAdmin() {

        mockAuthenticatedUser(userAdmin);

        when(repository.findAll()).thenReturn(List.of(task));
        when(mapper.toDTO(task)).thenReturn(taskDTO);

        List<RepairTaskDTO> result = service.getAll();

        assertThat(result).hasSize(1);
    }

    @Test
    void getAll_ShouldThrow_WhenNotAdmin() {

        mockAuthenticatedUser(userCustomer);

        assertThatThrownBy(service::getAll)
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Only admins can view all tasks");
    }

    @Test
    void update_ShouldUpdateTask_WhenAdmin() {

        mockAuthenticatedUser(userAdmin);

        when(repository.findById(100L)).thenReturn(Optional.of(task));
        when(repository.save(any())).thenReturn(task);
        when(mapper.toDTO(task)).thenReturn(taskDTO);

        RepairTaskDTO result = service.update(100L, taskDTO);

        assertThat(result).isNotNull();
    }

    @Test
    void update_ShouldThrow_WhenTaskNotFound() {

        mockAuthenticatedUser(userAdmin);

        when(repository.findById(100L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.update(100L, taskDTO))
                .isInstanceOf(RepairTaskNotFoundException.class);
    }

    @Test
    void delete_ShouldDelete_WhenAdmin() {

        mockAuthenticatedUser(userAdmin);

        when(repository.existsById(100L)).thenReturn(true);

        service.delete(100L);

        verify(repository).deleteById(100L);
    }

    @Test
    void delete_ShouldThrow_WhenTaskNotFound() {

        mockAuthenticatedUser(userAdmin);

        when(repository.existsById(100L)).thenReturn(false);

        assertThatThrownBy(() -> service.delete(100L))
                .isInstanceOf(RepairTaskNotFoundException.class);
    }
}

