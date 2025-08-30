package com.crudSE.demo.models.Employee;

import com.crudSE.demo.DTOs.EmployeeDTO;

public class EmployeeMapper {
  public static EmployeeDTO mapToEmployeeDTO(Employee employee){
    return new EmployeeDTO(
        employee.getId(),
        employee.getName(),
        employee.getEmail(),
        employee.getAddress(),
        employee.getPhone()
    );
  }
}


