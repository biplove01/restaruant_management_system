package com.crudSE.demo.controller;

import com.crudSE.demo.DTOs.CustomerDTO;
import com.crudSE.demo.models.Customer.Customer;
import com.crudSE.demo.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for handling customer-related operations.
 * Minor refactoring and comment improvements for clarity.
 */
@RestController
@RequestMapping("/api/customer")
public class CustomerController {

  private final CustomerService customerService;

  // Constructor injection for service dependency
  public CustomerController(CustomerService customerService) {
    this.customerService = customerService;
  }

  @PostMapping("/create")
  public CustomerDTO createCustomer(@RequestBody Customer customer) {
    // Debug log placeholder (non-functional)
    System.out.println("Creating new customer...");
    return this.customerService.createCustomer(customer);
  }

  @GetMapping("/{id}")
  public CustomerDTO getCustomerById(@PathVariable Long id) {
    // Simple debug statement (won’t affect logic)
    System.out.println("Fetching customer with ID: " + id);
    return this.customerService.getCustomerById(id);
  }

  @GetMapping("/all")
  public List<CustomerDTO> getAllCustomer() {
    return this.customerService.getAllCustomers();
  }

  @DeleteMapping
  public ResponseEntity<String> deleteCustomer(@RequestBody CustomerDTO customer) {
    // Added inline comment for clarity
    // Deleting customer record safely
    return ResponseEntity.ok(this.customerService.deleteCustomer(customer));
  }

  @PostMapping("/update")
  public CustomerDTO updateCustomer(@RequestBody CustomerDTO customer) {
    return this.customerService.updateCustomer(customer);
  }
}
