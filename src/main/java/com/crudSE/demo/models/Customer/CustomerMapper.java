package com.crudSE.demo.models.Customer;

import com.crudSE.demo.DTOs.CustomerDTO;

public class CustomerMapper {
    public static CustomerDTO mapToCustomerDTO(Customer customer){
    return new CustomerDTO(
        customer.getId(),
        customer.getName(),
        customer.getEmail(),
        customer.getAddress(),
        customer.getPhone()
    );
  }

  public static Customer mapToCustomer(CustomerDTO customerDTO){
    return new Customer(
        customerDTO.getId(),
        customerDTO.getName(),
        customerDTO.getEmail(),
        customerDTO.getAddress(),
        customerDTO.getPhone()
    );
  }
}
