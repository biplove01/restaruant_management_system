package com.crudSE.demo.service;

import com.crudSE.demo.GlobalExceptionHandler.CustomExceptions.AlreadyExistsException;
import com.crudSE.demo.GlobalExceptionHandler.CustomExceptions.ResourceNotFoundException;
import com.crudSE.demo.DTOs.CustomerDTO;
import com.crudSE.demo.models.Customer.Customer;
import com.crudSE.demo.models.Customer.CustomerMapper;
import com.crudSE.demo.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
  
  private final CustomerRepository customerRepository;
  
  public CustomerService(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }
  
  public CustomerDTO createCustomer(Customer customer){
    if(this.customerRepository.existsByEmail(customer.getEmail())){
      throw new AlreadyExistsException("The Customer with email: " + customer.getEmail() + " already exists");
    }
    
    
    return CustomerMapper.mapToCustomerDTO( this.customerRepository.save(customer));
  }
  
  public CustomerDTO getCustomerById(Long id) {
    
    
    
    return CustomerMapper.mapToCustomerDTO(this.customerRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("The user does not exists with id: "+ id )));
  }
  
  public List<CustomerDTO> getAllCustomers() {
    return this.customerRepository.findAll().stream()
        .map(CustomerMapper::mapToCustomerDTO).collect(
            java.util.stream.Collectors.toList()
        );
  }
  
  
  public String deleteCustomer(CustomerDTO customer) {
    
    if(!this.customerRepository.existsByEmail(customer.getEmail())){
      throw new ResourceNotFoundException("Customer with email: " + customer.getEmail() + " does not exists");
    }
    
    this.customerRepository.deleteById(customer.getId());
    return "User of id " + customer.getId() + " successfully deleted";
  }
  
  public CustomerDTO updateCustomer(CustomerDTO customer) {
    Customer customerToUpdate = this.customerRepository.findById(customer.getId()).orElseThrow(()-> new ResourceNotFoundException("The customer with id "+ customer.getId()+ " is not found"));
    
    customerToUpdate.setAddress(customer.getAddress());
    customerToUpdate.setPhone(customer.getPhone());
    customerToUpdate.setEmail(customer.getEmail());
    customerToUpdate.setName(customer.getName());
    return CustomerMapper.mapToCustomerDTO(this.customerRepository.save(customerToUpdate));
  }
  
}
