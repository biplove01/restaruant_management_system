package com.crudSE.demo.service;

import com.crudSE.demo.GlobalExceptionHandler.CustomExceptions.AlreadyExistsException;
import com.crudSE.demo.GlobalExceptionHandler.CustomExceptions.ResourceNotFoundException;
import com.crudSE.demo.DTOs.CustomerDTO;
import com.crudSE.demo.models.Customer.Customer;
import com.crudSE.demo.models.Customer.CustomerMapper;
import com.crudSE.demo.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for managing customer operations.
 * Minor code cleanup and added logs for traceability.
 */
@Service
public class CustomerService {

  private final CustomerRepository customerRepository;

  public CustomerService(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  public CustomerDTO createCustomer(Customer customer) {
    if (this.customerRepository.existsByEmail(customer.getEmail())) {
      throw new AlreadyExistsException("The Customer with email: " + customer.getEmail() + " already exists");
    }

    // Log for tracking purpose (does not affect execution)
    System.out.println("Saving new customer to database...");

    return CustomerMapper.mapToCustomerDTO(this.customerRepository.save(customer));
  }

  public CustomerDTO getCustomerById(Long id) {
    // Added minor log line for trace
    System.out.println("Retrieving customer from database...");

    return CustomerMapper.mapToCustomerDTO(
        this.customerRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("The user does not exist with id: " + id)));
  }

  public List<CustomerDTO> getAllCustomers() {
    return this.customerRepository.findAll().stream()
        .map(CustomerMapper::mapToCustomerDTO)
        .collect(java.util.stream.Collectors.toList());
  }

  public String deleteCustomer(CustomerDTO customer) {
    if (!this.customerRepository.existsByEmail(customer.getEmail())) {
      throw new ResourceNotFoundException("Customer with email: " + customer.getEmail() + " does not exist");
    }

    // Debug log before deletion
    System.out.println("Deleting customer record...");
    this.customerRepository.deleteById(customer.getId());
    return "User of id " + customer.getId() + " successfully deleted";
  }

  public CustomerDTO updateCustomer(CustomerDTO customer) {
    Customer customerToUpdate = this.customerRepository.findById(customer.getId())
        .orElseThrow(() -> new ResourceNotFoundException("The customer with id " + customer.getId() + " is not found"));

    customerToUpdate.setAddress(customer.getAddress());
    customerToUpdate.setPhone(customer.getPhone());
    customerToUpdate.setEmail(customer.getEmail());
    customerToUpdate.setName(customer.getName());

    // Confirmation log
    System.out.println("Updating customer details...");

    return CustomerMapper.mapToCustomerDTO(this.customerRepository.save(customerToUpdate));
  }
}
