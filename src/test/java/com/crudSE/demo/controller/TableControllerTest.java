package com.crudSE.demo.controller;

import com.crudSE.demo.GlobalExceptionHandler.CustomExceptions.AlreadyExistsException;
import com.crudSE.demo.GlobalExceptionHandler.CustomExceptions.ResourceNotFoundException;
import com.crudSE.demo.models.Table;
import com.crudSE.demo.service.OrderListService;
import com.crudSE.demo.service.TableService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TableController.class)
class TableControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TableService tableService;

    @MockBean
    private OrderListService orderListService;

    @Autowired
    private ObjectMapper objectMapper;

    private Table table;

    @BeforeEach
    void setUp() {
        table = new Table();
        table.setId(1L);
        table.setTableNumber("T1");
        table.setOrderList(new ArrayList<>());
    }

    @Test
    void createTable_Success() throws Exception {
        when(tableService.createTable(any(Table.class))).thenReturn(table);

        mockMvc.perform(post("/api/table/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(table)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.tableNumber").value("T1"));

        verify(tableService, times(1)).createTable(any(Table.class));
    }

    @Test
    void createTable_AlreadyExistsException() throws Exception {
        when(tableService.createTable(any(Table.class)))
                .thenThrow(new AlreadyExistsException("The table with number: T1 already exists"));

        mockMvc.perform(post("/api/table/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(table)))
                .andExpect(status().isBadRequest());

        verify(tableService, times(1)).createTable(any(Table.class));
    }

    @Test
    void getTableById_Success() throws Exception {
        when(tableService.getTableById(1L)).thenReturn(table);

        mockMvc.perform(get("/api/table/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.tableNumber").value("T1"));

        verify(tableService, times(1)).getTableById(1L);
    }

    @Test
    void getTableById_NotFound() throws Exception {
        when(tableService.getTableById(999L))
                .thenThrow(new ResourceNotFoundException("The table does not exist with id: 999"));

        mockMvc.perform(get("/api/table/999"))
                .andExpect(status().isNotFound());

        verify(tableService, times(1)).getTableById(999L);
    }

    @Test
    void getAllTables_Success() throws Exception {
        Table table2 = new Table();
        table2.setId(2L);
        table2.setTableNumber("T2");
        table2.setOrderList(new ArrayList<>());

        List<Table> tables = Arrays.asList(table, table2);
        when(tableService.getAllTables()).thenReturn(tables);

        mockMvc.perform(get("/api/table/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].tableNumber").value("T1"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].tableNumber").value("T2"));

        verify(tableService, times(1)).getAllTables();
    }

    @Test
    void getAllTables_EmptyList() throws Exception {
        when(tableService.getAllTables()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/table/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(tableService, times(1)).getAllTables();
    }

    @Test
    void updateTable_Success() throws Exception {
        table.setTableNumber("T1-UPDATED");
        when(tableService.updateTable(eq(1L), any(Table.class))).thenReturn(table);

        mockMvc.perform(post("/api/table/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(table)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tableNumber").value("T1-UPDATED"));

        verify(tableService, times(1)).updateTable(eq(1L), any(Table.class));
    }

    @Test
    void updateTable_NotFound() throws Exception {
        when(tableService.updateTable(eq(999L), any(Table.class)))
                .thenThrow(new ResourceNotFoundException("The table with id 999 is not found"));

        mockMvc.perform(post("/api/table/update/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(table)))
                .andExpect(status().isNotFound());

        verify(tableService, times(1)).updateTable(eq(999L), any(Table.class));
    }

    @Test
    void deleteTable_Success() throws Exception {
        when(tableService.deleteTable(1L)).thenReturn("Table with id 1 successfully deleted");

        mockMvc.perform(delete("/api/table/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Table with id 1 successfully deleted"));

        verify(tableService, times(1)).deleteTable(1L);
    }

    @Test
    void deleteTable_NotFound() throws Exception {
        when(tableService.deleteTable(999L))
                .thenThrow(new ResourceNotFoundException("Table with id: 999 does not exist"));

        mockMvc.perform(delete("/api/table/999"))
                .andExpect(status().isNotFound());

        verify(tableService, times(1)).deleteTable(999L);
    }
}
