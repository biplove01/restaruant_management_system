package com.crudSE.demo.controller;

import com.crudSE.demo.DTOs.CustomerDTO;
import com.crudSE.demo.models.Customer.Customer;
import com.crudSE.demo.service.CustomerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
  
  private final CustomerService customerService;
  
  public CustomerController( CustomerService customerService) {
    this.customerService = customerService;
  }
  
  @PostMapping("/create")
  public CustomerDTO createCustomer(@RequestBody Customer customer){
    return this.customerService.createCustomer(customer);
  }
  
  @GetMapping("/{id}")
  public CustomerDTO getCustomerById(@PathVariable Long id){
    return this.customerService.getCustomerById(id);
  }
  
  @GetMapping("/all")
  public List<CustomerDTO> getAllCustomer(){
    return this.customerService.getAllCustomers();
  }
  
  @DeleteMapping
  public ResponseEntity<String> deleteCustomer(@RequestBody CustomerDTO customer){
    return ResponseEntity.ok(this.customerService.deleteCustomer(customer));
  }
  
  @PostMapping("/update")
  public CustomerDTO updateCustomer(@RequestBody CustomerDTO customer){
    return this.customerService.updateCustomer(customer);
  }
  
  
  
}
