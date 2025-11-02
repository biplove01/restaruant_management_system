package com.crudSE.demo.controller;

import com.crudSE.demo.DTOs.EmployeeDTO;
import com.crudSE.demo.GlobalExceptionHandler.CustomExceptions.AlreadyExistsException;
import com.crudSE.demo.GlobalExceptionHandler.CustomExceptions.ResourceNotFoundException;
import com.crudSE.demo.models.Employee.Employee;
import com.crudSE.demo.models.enums.RoleStatus;
import com.crudSE.demo.service.EmployeeService;
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

@WebMvcTest(EmployeeController.class)
class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Employee employee;
    private EmployeeDTO employeeDTO;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setId(1L);
        employee.setName("John Doe");
        employee.setEmail("john.doe@example.com");
        employee.setAddress("123 Main St");
        employee.setPhone(1234567890L);
        employee.setPassword("password123");
        employee.setRole(RoleStatus.WAITER);

        employeeDTO = new EmployeeDTO();
        employeeDTO.setId(1L);
        employeeDTO.setName("John Doe");
        employeeDTO.setEmail("john.doe@example.com");
        employeeDTO.setAddress("123 Main St");
        employeeDTO.setPhone(1234567890L);
    }

    @Test
    void createEmployee_Success() throws Exception {
        when(employeeService.createEmployee(any(Employee.class))).thenReturn(employeeDTO);

        mockMvc.perform(post("/api/employee/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.address").value("123 Main St"))
                .andExpect(jsonPath("$.phone").value(1234567890L));

        verify(employeeService, times(1)).createEmployee(any(Employee.class));
    }

    @Test
    void createEmployee_AlreadyExistsException() throws Exception {
        when(employeeService.createEmployee(any(Employee.class)))
                .thenThrow(new AlreadyExistsException("The Employee with email: " + employee.getEmail() + " already exists"));

        mockMvc.perform(post("/api/employee/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isBadRequest());

        verify(employeeService, times(1)).createEmployee(any(Employee.class));
    }

    @Test
    void getEmployeeById_Success() throws Exception {
        when(employeeService.getEmployeeById(1L)).thenReturn(employeeDTO);

        mockMvc.perform(get("/api/employee/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(employeeService, times(1)).getEmployeeById(1L);
    }

    @Test
    void getEmployeeById_NotFound() throws Exception {
        when(employeeService.getEmployeeById(999L))
                .thenThrow(new ResourceNotFoundException("The user does not exists with id: 999"));

        mockMvc.perform(get("/api/employee/999"))
                .andExpect(status().isNotFound());

        verify(employeeService, times(1)).getEmployeeById(999L);
    }

    @Test
    void getAllEmployees_Success() throws Exception {
        EmployeeDTO employeeDTO2 = new EmployeeDTO();
        employeeDTO2.setId(2L);
        employeeDTO2.setName("Jane Smith");
        employeeDTO2.setEmail("jane.smith@example.com");
        employeeDTO2.setAddress("456 Oak Ave");
        employeeDTO2.setPhone(9876543210L);

        List<EmployeeDTO> employees = Arrays.asList(employeeDTO, employeeDTO2);
        when(employeeService.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(get("/api/employee/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].name").value("Jane Smith"));

        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    void getAllEmployees_EmptyList() throws Exception {
        when(employeeService.getAllEmployees()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/employee/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(employeeService, times(1)).getAllEmployees();
    }

    @Test
    void updateEmployee_Success() throws Exception {
        employeeDTO.setName("John Updated");
        employeeDTO.setAddress("789 New St");
        when(employeeService.updateEmployee(any(EmployeeDTO.class))).thenReturn(employeeDTO);

        mockMvc.perform(post("/api/employee/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Updated"))
                .andExpect(jsonPath("$.address").value("789 New St"));

        verify(employeeService, times(1)).updateEmployee(any(EmployeeDTO.class));
    }

    @Test
    void updateEmployee_NotFound() throws Exception {
        employeeDTO.setId(999L);
        when(employeeService.updateEmployee(any(EmployeeDTO.class)))
                .thenThrow(new ResourceNotFoundException("The employee with id 999 is not found"));

        mockMvc.perform(post("/api/employee/update")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDTO)))
                .andExpect(status().isNotFound());

        verify(employeeService, times(1)).updateEmployee(any(EmployeeDTO.class));
    }

    @Test
    void deleteEmployee_Success() throws Exception {
        when(employeeService.deleteEmployee(1L)).thenReturn("User of id 1 successfully deleted");

        mockMvc.perform(delete("/api/employee/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("User of id 1 successfully deleted"));

        verify(employeeService, times(1)).deleteEmployee(1L);
    }

    @Test
    void deleteEmployee_NotFound() throws Exception {
        when(employeeService.deleteEmployee(999L))
                .thenThrow(new ResourceNotFoundException("Employee with id: 999 does not exists"));

        mockMvc.perform(delete("/api/employee/999"))
                .andExpect(status().isNotFound());

        verify(employeeService, times(1)).deleteEmployee(999L);
    }
}
