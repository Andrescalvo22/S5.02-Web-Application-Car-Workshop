package com.workshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workshop.dto.RepairTaskDTO;
import com.workshop.service.RepairTaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class RepairTaskControllerTest {

    @Mock
    private RepairTaskService service;

    @InjectMocks
    private RepairTaskController controller;

    private MockMvc mockMvc;
    private ObjectMapper mapper;

    @BeforeEach
    void setup() {
        mapper = new ObjectMapper();
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    void createTask_shouldReturn201() throws Exception {

        RepairTaskDTO dto = new RepairTaskDTO();
        dto.setTask("Change oil");
        dto.setCost(50.0);

        RepairTaskDTO response = new RepairTaskDTO();
        response.setId(1L);
        response.setTask("Change oil");
        response.setCost(50.0);

        when(service.create(eq(10L), any(RepairTaskDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/tasks/order/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.task").value("Change oil"));
    }

    @Test
    void getAllTasks_shouldReturn200() throws Exception {

        RepairTaskDTO dto = new RepairTaskDTO();
        dto.setId(1L);
        dto.setTask("Fix brakes");

        when(service.getAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].task").value("Fix brakes"));
    }

    @Test
    void getTaskById_shouldReturn200() throws Exception {

        RepairTaskDTO dto = new RepairTaskDTO();
        dto.setId(5L);
        dto.setTask("Wheel alignment");

        when(service.getById(5L)).thenReturn(dto);

        mockMvc.perform(get("/api/tasks/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.task").value("Wheel alignment"));
    }

    @Test
    void getTasksByOrderId_shouldReturn200() throws Exception {

        RepairTaskDTO dto = new RepairTaskDTO();
        dto.setId(7L);
        dto.setTask("Replace battery");

        when(service.getByOrderId(3L)).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/tasks/order/3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].task").value("Replace battery"));
    }

    @Test
    void updateTask_shouldReturn200() throws Exception {

        RepairTaskDTO dto = new RepairTaskDTO();
        dto.setTask("Updated task");
        dto.setCost(100.0);

        RepairTaskDTO updated = new RepairTaskDTO();
        updated.setId(9L);
        updated.setTask("Updated task");
        updated.setCost(100.0);

        when(service.update(eq(9L), any(RepairTaskDTO.class))).thenReturn(updated);

        mockMvc.perform(put("/api/tasks/9")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.task").value("Updated task"));
    }

    @Test
    void deleteTask_shouldReturn204() throws Exception {

        doNothing().when(service).delete(4L);

        mockMvc.perform(delete("/api/tasks/4"))
                .andExpect(status().isNoContent());
    }
}

