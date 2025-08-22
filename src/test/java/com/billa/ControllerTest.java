package com.billa;

import com.billa.controllers.FlywayController;
import com.billa.jooq.tables.pojos.Orders;
import com.billa.repository.FlywayRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FlywayController.class)
class ControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FlywayRepository repository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testShowEndpoint() throws Exception {
        mockMvc.perform(get("/v1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Welcome to Application"));
    }

    @Test
    void testSaveAllOrders() throws Exception {
        Orders order1 = new Orders(null, 1234L, 100);
        Orders order2 = new Orders(null, 3452L, 200);
        List<Orders> inputOrders = Arrays.asList(order1, order2);

        Orders saved1 = new Orders(1L, 1234L, 100);
        Orders saved2 = new Orders(2L, 3452L, 200);
        List<Orders> savedOrders = Arrays.asList(saved1, saved2);

        when(repository.saveAll(anyList())).thenReturn(savedOrders);

        mockMvc.perform(post("/v1/saveAll")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputOrders)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].amount").value(100))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].amount").value(200));
    }

    @Test
    void testSave() throws Exception {
        Orders order1 = new Orders(null, 1234L, 100);
        Orders saved1 = new Orders(1L, 1234L, 100);

        when(repository.save(any(Orders.class))).thenReturn(saved1);

        ResultActions resultActions = mockMvc.perform(post("/v1/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(100));

        System.out.println(resultActions.andReturn().getResponse().getContentAsString());
    }

    @Test
    void testUpdateOrder() throws Exception {
        Orders orderToUpdate = new Orders(1L, 1234L, 150);
        Orders updatedOrder = new Orders(1L, 1234L, 150);

        when(repository.update(any(Orders.class))).thenReturn(updatedOrder);

        ResultActions resultActions = mockMvc.perform(put("/v1/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(150));

        System.out.println(resultActions.andReturn().getResponse().getContentAsString());
    }

    @Test
    void testFetchAllOrders() throws Exception {
        // Prepare mocked orders
        Orders order1 = new Orders(1L, 1234L, 100);
        Orders order2 = new Orders(2L, 3452L, 200);
        List<Orders> allOrders = Arrays.asList(order1, order2);

        // Mock repository behavior
        when(repository.findAll()).thenReturn(allOrders);

        // Perform GET request
        mockMvc.perform(get("/v1/fetchAll")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].amount").value(100))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].amount").value(200));
    }

    @Test
    void testFetchOneOrder() throws Exception {
        Orders order = new Orders(1L, 1234L, 100);

        // Mock repository behavior
        when(repository.findById(1L)).thenReturn(order);

        mockMvc.perform(get("/v1/fetch/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.amount").value(100));
    }

    @Test
    void testDeleteOrder() throws Exception {
        // Mock repository behavior: assume deletion successful
        when(repository.deleteById(1L)).thenReturn(1);

        mockMvc.perform(delete("/v1/delete/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }


}
