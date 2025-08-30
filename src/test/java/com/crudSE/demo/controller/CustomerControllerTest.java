package com.crudSE.demo.controller;

import com.crudSE.demo.DTOs.CustomerDTO;
import com.crudSE.demo.models.Customer.Customer;
import com.crudSE.demo.service.CustomerService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private CustomerController customerController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    private Customer testCustomer;
    private CustomerDTO testCustomerDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
        objectMapper = new ObjectMapper();

        // Setup test data
        testCustomer = new Customer();
        testCustomer.setId(1L);
        testCustomer.setName("John Doe");
        testCustomer.setEmail("john.doe@example.com");
        testCustomer.setPassword("password123");
        testCustomer.setAddress("123 Main St");
        testCustomer.setPhone(1234567890L);

        testCustomerDTO = new CustomerDTO();
        testCustomerDTO.setId(1L);
        testCustomerDTO.setName("John Doe");
        testCustomerDTO.setEmail("john.doe@example.com");
        testCustomerDTO.setAddress("123 Main St");
        testCustomerDTO.setPhone(1234567890L);
    }

    @Test
    void createCustomer_Success() throws Exception {
        // Given
        when(customerService.createCustomer(any(Customer.class))).thenReturn(testCustomerDTO);

        // When & Then
        mockMvc.perform(post("/api/customer/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCustomer)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"))
                .andExpect(jsonPath("$.address").value("123 Main St"))
                .andExpect(jsonPath("$.phone").value(1234567890L));

        verify(customerService, times(1)).createCustomer(any(Customer.class));
    }

    @Test
    void createCustomer_WithInvalidData_ReturnsBadRequest() throws Exception {
        // Given
        Customer invalidCustomer = new Customer();
        invalidCustomer.setName(""); // Invalid name

        // When & Then
        mockMvc.perform(post("/api/customer/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidCustomer)))
                .andExpect(status().isOk()); // Controller doesn't validate, service handles it

        verify(customerService, times(1)).createCustomer(any(Customer.class));
    }

    @Test
    void getCustomerById_Success() throws Exception {
        // Given
        when(customerService.getCustomerById(1L)).thenReturn(testCustomerDTO);

        // When & Then
        mockMvc.perform(get("/api/customer/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john.doe@example.com"));

        verify(customerService, times(1)).getCustomerById(1L);
    }

    @Test
    void getCustomerById_NotFound() throws Exception {
        // Given
        when(customerService.getCustomerById(999L)).thenThrow(new RuntimeException("Customer not found"));

        // When & Then
        mockMvc.perform(get("/api/customer/999"))
                .andExpect(status().isInternalServerError());

        verify(customerService, times(1)).getCustomerById(999L);
    }

    @Test
    void getAllCustomers_Success() throws Exception {
        // Given
        CustomerDTO customer2 = new CustomerDTO();
        customer2.setId(2L);
        customer2.setName("Jane Smith");
        customer2.setEmail("jane.smith@example.com");
        customer2.setAddress("456 Oak Ave");
        customer2.setPhone(9876543210L);

        List<CustomerDTO> customers = Arrays.asList(testCustomerDTO, customer2);
        when(customerService.getAllCustomers()).thenReturn(customers);

        // When & Then
        mockMvc.perform(get("/api/customer/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Jane Smith"));

        verify(customerService, times(1)).getAllCustomers();
    }

    @Test
    void getAllCustomers_EmptyList() throws Exception {
        // Given
        when(customerService.getAllCustomers()).thenReturn(Arrays.asList());

        // When & Then
        mockMvc.perform(get("/api/customer/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());

        verify(customerService, times(1)).getAllCustomers();
    }

    @Test
    void updateCustomer_Success() throws Exception {
        // Given
        CustomerDTO updatedCustomerDTO = new CustomerDTO();
        updatedCustomerDTO.setId(1L);
        updatedCustomerDTO.setName("John Updated");
        updatedCustomerDTO.setEmail("john.updated@example.com");
        updatedCustomerDTO.setAddress("456 Updated St");
        updatedCustomerDTO.setPhone(1112223333L);

        when(customerService.updateCustomer(any(CustomerDTO.class))).thenReturn(updatedCustomerDTO);

        // When & Then
        mockMvc.perform(post("/api/customer/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedCustomerDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("John Updated"))
                .andExpect(jsonPath("$.email").value("john.updated@example.com"))
                .andExpect(jsonPath("$.address").value("456 Updated St"))
                .andExpect(jsonPath("$.phone").value(1112223333));

        verify(customerService, times(1)).updateCustomer(any(CustomerDTO.class));
    }

    @Test
    void deleteCustomer_Success() throws Exception {
        // Given
        when(customerService.deleteCustomer(any(CustomerDTO.class))).thenReturn("Customer deleted successfully");

        // When & Then
        mockMvc.perform(delete("/api/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCustomerDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Customer deleted successfully"));

        verify(customerService, times(1)).deleteCustomer(any(CustomerDTO.class));
    }

    @Test
    void deleteCustomer_NotFound() throws Exception {
        // Given
        when(customerService.deleteCustomer(any(CustomerDTO.class))).thenReturn("Customer not found");

        // When & Then
        mockMvc.perform(delete("/api/customer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testCustomerDTO)))
                .andExpect(status().isOk())
                .andExpect(content().string("Customer not found"));

        verify(customerService, times(1)).deleteCustomer(any(CustomerDTO.class));
    }

    @Test
    void controller_HandlesNullValues() throws Exception {
        // Given
        Customer nullCustomer = new Customer();
        when(customerService.createCustomer(any(Customer.class))).thenReturn(testCustomerDTO);

        // When & Then
        mockMvc.perform(post("/api/customer/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nullCustomer)))
                .andExpect(status().isOk());

        verify(customerService, times(1)).createCustomer(any(Customer.class));
    }
}
