package com.crudSE.demo.controller;

import com.crudSE.demo.DTOs.EmployeeDTO;
import com.crudSE.demo.models.Employee.Employee;
import com.crudSE.demo.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private Employee testEmployee;
    private EmployeeDTO testEmployeeDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
        objectMapper = new ObjectMapper();

        // Setup test data
        testEmployee = new Employee();
        testEmployee.setId(1L);
        testEmployee.setName("Jane Smith");
        testEmployee.setEmail("jane.smith@example.com");
        testEmployee.setPassword("employee123");
        testEmployee.setAddress("456 Employee St");
        testEmployee.setPhone(9876543210L);

        testEmployeeDTO = new EmployeeDTO();
        testEmployeeDTO.setId(1L);
        testEmployeeDTO.setName("Jane Smith");
        testEmployeeDTO.setEmail("jane.smith@example.com");
        testEmployeeDTO.setAddress("456 Employee St");
        testEmployeeDTO.setPhone(9876543210L);
    }

    @Test
    void createEmployee_Success() throws Exception {
        // Given
        when(employeeService.createEmployee(any(Employee.class))).thenReturn(testEmployeeDTO);

        // When & Then
        mockMvc.perform(post("/api/employee/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testEmployee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Jane Smith"))
                .andExpect(jsonPath("$.email").value("jane.smith@example.com"))
                .andExpect(jsonPath("$.address").value("456 Employee St"))
                .andExpect(jsonPath("$.phone").value(9876543210L));

        verify(employeeService, times(1)).createEmployee(any(Employee.class));
    }

    @Test
    void createEmployee_WithInvalidData_ReturnsOk() throws Exception {
        // Given
        Employee invalidEmployee = new Employee();
        invalidEmployee.setName(""); // Invalid name

        // When & Then
        mockMvc.perform(post("/api/employee/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidEmployee)))
                .andExpect(status().isOk()); // Controller doesn't validate, service handles it

        verify(employeeService, times(1)).createEmployee(any(Employee.class));
    }

    @Test
    void getEmployeeById_Success() throws Exception {
        // Given
        when(employeeService.getEmployeeById(1L)).thenReturn(testEmployeeDTO);

        // When & Then
        mockMvc.perform(get("/api/employee/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Jane Smith"))
                .andExpect(jsonPath("$.email").value("jane.smith@example.com"));

        verify(employeeService, times(1)).getEmployeeById(1L);
    }

    @Test
    void getEmployeeById_NotFound() throws Exception {
        // Given
        when(employeeService.getEmployeeById(999L)).thenThrow(new RuntimeException("Employee not found"));

        // When & Then
        mockMvc.perform(get("/api/employee/999"))
                .andExpect(status().isInternalServerError());

        verify(employeeService, times(1)).getEmployeeById(999L);
    }

    @Test
    void getAllEmployees_Success() throws Exception {
        // Given
        EmployeeDTO employee2 = new EmployeeDTO();
        employee2.setId(2L);
        employee2.setName("Bob Johnson");
        employee2.setEmail("bob.johnson@example.com");
        employee2.setAddress("789 Worker Ave");
        employee2.setPhone(5556667777L);

        List<EmployeeDTO> employees = Arrays.asList(testEmployeeDTO, employee2);
        when(employeeService.getAllEmployees()).thenReturn(employees);

        // When & Then
        mockMvc.perform(get("/api/employee/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Jane Smith"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Bob Johnson"));

        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    void getAllEmployees_EmptyList() throws Exception {
        // Given
        when(employeeService.getAllEmployees()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/employee/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    void updateEmployee_Success() throws Exception {
        // Given
        EmployeeDTO updatedEmployeeDTO = new EmployeeDTO();
        updatedEmployeeDTO.setId(1L);
        updatedEmployeeDTO.setName("Jane Updated");
        updatedEmployeeDTO.setEmail("jane.updated@example.com");
        updatedEmployeeDTO.setAddress("789 Updated St");
        updatedEmployeeDTO.setPhone(1112223333L);

        when(employeeService.updateEmployee(any(EmployeeDTO.class))).thenReturn(updatedEmployeeDTO);

        // When & Then
        mockMvc.perform(post("/api/employee/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployeeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Jane Updated"))
                .andExpect(jsonPath("$.email").value("jane.updated@example.com"))
                .andExpect(jsonPath("$.address").value("789 Updated St"))
                .andExpect(jsonPath("$.phone").value(1112223333));

        verify(employeeService, times(1)).updateEmployee(any(EmployeeDTO.class));
    }

    @Test
    void deleteEmployee_Success() throws Exception {
        // Given
        when(employeeService.deleteEmployee(any(EmployeeDTO.class))).thenReturn("Employee deleted successfully");

        // When & Then
        mockMvc.perform(delete("/api/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testEmployeeDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee deleted successfully"));

        verify(employeeService, times(1)).deleteEmployee(any(EmployeeDTO.class));
    }

    @Test
    void deleteEmployee_NotFound() throws Exception {
        // Given
        when(employeeService.deleteEmployee(any(EmployeeDTO.class))).thenReturn("Employee not found");

        // When & Then
        mockMvc.perform(delete("/api/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testEmployeeDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Employee not found"));

        verify(employeeService, times(1)).deleteEmployee(any(EmployeeDTO.class));
    }

    @Test
    void controller_HandlesNullValues() throws Exception {
        // Given
        Employee nullEmployee = new Employee();
        when(employeeService.createEmployee(any(Employee.class))).thenReturn(testEmployeeDTO);

        // When & Then
        mockMvc.perform(post("/api/employee/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nullEmployee)))
                .andExpect(status().isOk());

        verify(employeeService, times(1)).createEmployee(any(Employee.class));
    }

    @Test
    void controller_HandlesEmptyRequestBody() throws Exception {
        // Given
        when(employeeService.createEmployee(any(Employee.class))).thenReturn(testEmployeeDTO);

        // When & Then
        mockMvc.perform(post("/api/employee/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isOk());

        verify(employeeService, times(1)).createEmployee(any(Employee.class));
    }
}
