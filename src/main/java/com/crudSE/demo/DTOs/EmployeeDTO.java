package com.crudSE.demo.DTOs;

import com.crudSE.demo.models.Role;
import com.crudSE.demo.models.enums.RoleStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
  
  private Long id;
  private String name;
  private String email;
  private String address;
  private Long phone;
  private RoleStatus role;
  
}

