package com.crudSE.demo.models.Customer;

import com.crudSE.demo.DTOs.CustomerDTO;
import com.crudSE.demo.models.OrderList;
import jakarta.persistence.OneToMany;

import java.util.List;

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
}
