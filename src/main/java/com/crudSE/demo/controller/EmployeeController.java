package com.crudSE.demo.controller;

import com.crudSE.demo.DTOs.EmployeeDTO;
import com.crudSE.demo.models.Employee.Employee;
import com.crudSE.demo.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
  
  private final EmployeeService employeeService;
  
  
  public EmployeeController( EmployeeService employeeService) {
    this.employeeService = employeeService;
  }
  
  @PostMapping("/create")
  public EmployeeDTO createEmployee(@RequestBody Employee employee){
   return this.employeeService.createEmployee(employee);
  }
  
  @GetMapping("/{id}")
  public EmployeeDTO getEmployeeById(@PathVariable Long id){
    return this.employeeService.getEmployeeById(id);
  }
  
  
  @GetMapping("/all")
  public List<EmployeeDTO> getAllEmployee(){
    return this.employeeService.getAllEmployees();
  }
  
  
  @DeleteMapping("{id}")
  public ResponseEntity<String> deleteEmployee(@PathVariable Long id){
    return ResponseEntity.ok(this.employeeService.deleteEmployee(id));
  }
  
  @PostMapping("/update")
  public EmployeeDTO updateEmployee(@RequestBody EmployeeDTO employee){
    return this.employeeService.updateEmployee(employee);
  }
}
