package com.workshop.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workshop.dto.RepairOrderDTO;
import com.workshop.service.RepairOrderService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class RepairOrderControllerTest {

    @Mock
    private RepairOrderService service;

    @InjectMocks
    private RepairOrderController controller;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void create_shouldReturn201() throws Exception {

        RepairOrderDTO dto = new RepairOrderDTO();
        RepairOrderDTO returnedDto = new RepairOrderDTO();
        returnedDto.setId(1L);

        when(service.create(eq(10L), any())).thenReturn(returnedDto);

        mockMvc.perform(post("/api/repair-orders/car/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void getAll_shouldReturn200() throws Exception {

        when(service.getAll()).thenReturn(List.of(new RepairOrderDTO()));

        mockMvc.perform(get("/api/repair-orders"))
                .andExpect(status().isOk());
    }

    @Test
    void getById_shouldReturn200() throws Exception {

        RepairOrderDTO dto = new RepairOrderDTO();
        dto.setId(5L);

        when(service.getById(5L)).thenReturn(dto);

        mockMvc.perform(get("/api/repair-orders/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5L));
    }

    @Test
    void getByCarId_shouldReturn200() throws Exception {

        when(service.getByCarId(7L)).thenReturn(List.of(new RepairOrderDTO()));

        mockMvc.perform(get("/api/repair-orders/car/7"))
                .andExpect(status().isOk());
    }

    @Test
    void update_shouldReturn200() throws Exception {

        RepairOrderDTO dto = new RepairOrderDTO();
        dto.setDescription("Updated");

        when(service.update(eq(3L), any())).thenReturn(dto);

        mockMvc.perform(put("/api/repair-orders/3")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Updated"));
    }

    @Test
    void closeOrder_shouldReturn204() throws Exception {

        Long id = 1L;

        when(service.closeOrder(id)).thenReturn(new RepairOrderDTO());

        mockMvc.perform(put("/api/repair-orders/{id}/close", id))
                .andExpect(status().isNoContent());

        verify(service).closeOrder(id);
    }

}


