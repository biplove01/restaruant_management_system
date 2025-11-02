package com.crudSE.demo.service;

import com.crudSE.demo.DTOs.EmployeeDTO;
import com.crudSE.demo.GlobalExceptionHandler.CustomExceptions.AlreadyExistsException;
import com.crudSE.demo.GlobalExceptionHandler.CustomExceptions.ResourceNotFoundException;
import com.crudSE.demo.models.Employee.Employee;
import com.crudSE.demo.models.enums.RoleStatus;
import com.crudSE.demo.repositories.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee employee;

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
    }

    @Test
    void createEmployee_Success() {
        when(employeeRepository.existsByEmail(employee.getEmail())).thenReturn(false);
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        EmployeeDTO result = employeeService.createEmployee(employee);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getName());
        assertEquals("john.doe@example.com", result.getEmail());
        assertEquals("123 Main St", result.getAddress());
        assertEquals(1234567890L, result.getPhone());
        verify(employeeRepository, times(1)).existsByEmail(employee.getEmail());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void createEmployee_AlreadyExistsException() {
        when(employeeRepository.existsByEmail(employee.getEmail())).thenReturn(true);

        assertThrows(AlreadyExistsException.class, () -> {
            employeeService.createEmployee(employee);
        });

        verify(employeeRepository, times(1)).existsByEmail(employee.getEmail());
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void getEmployeeById_Success() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        EmployeeDTO result = employeeService.getEmployeeById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getName());
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void getEmployeeById_NotFound() {
        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getEmployeeById(999L);
        });

        verify(employeeRepository, times(1)).findById(999L);
    }

    @Test
    void getAllEmployees_Success() {
        Employee employee2 = new Employee();
        employee2.setId(2L);
        employee2.setName("Jane Smith");
        employee2.setEmail("jane.smith@example.com");
        employee2.setAddress("456 Oak Ave");
        employee2.setPhone(9876543210L);
        employee2.setRole(RoleStatus.WAITER);

        List<Employee> employees = Arrays.asList(employee, employee2);
        when(employeeRepository.findAll()).thenReturn(employees);

        List<EmployeeDTO> result = employeeService.getAllEmployees();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void getAllEmployees_EmptyList() {
        when(employeeRepository.findAll()).thenReturn(new ArrayList<>());

        List<EmployeeDTO> result = employeeService.getAllEmployees();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(employeeRepository, times(1)).findAll();
    }

    @Test
    void updateEmployee_Success() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(1L);
        employeeDTO.setName("John Updated");
        employeeDTO.setEmail("john.updated@example.com");
        employeeDTO.setAddress("789 New St");
        employeeDTO.setPhone(1112223333L);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        EmployeeDTO result = employeeService.updateEmployee(employeeDTO);

        assertNotNull(result);
        verify(employeeRepository, times(1)).findById(1L);
        verify(employeeRepository, times(1)).save(any(Employee.class));
        assertEquals("789 New St", employee.getAddress());
        assertEquals("John Updated", employee.getName());
    }

    @Test
    void updateEmployee_NotFound() {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(999L);

        when(employeeRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.updateEmployee(employeeDTO);
        });

        verify(employeeRepository, times(1)).findById(999L);
        verify(employeeRepository, never()).save(any(Employee.class));
    }

    @Test
    void deleteEmployee_Success() {
        when(employeeRepository.existsById(1L)).thenReturn(true);
        doNothing().when(employeeRepository).deleteById(1L);

        String result = employeeService.deleteEmployee(1L);

        assertEquals("User of id 1 successfully deleted", result);
        verify(employeeRepository, times(1)).existsById(1L);
        verify(employeeRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteEmployee_NotFound() {
        when(employeeRepository.existsById(999L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.deleteEmployee(999L);
        });

        verify(employeeRepository, times(1)).existsById(999L);
        verify(employeeRepository, never()).deleteById(anyLong());
    }
}
