package com.crudSE.demo.controller;

import com.crudSE.demo.DTOs.CustomerDTO;
import com.crudSE.demo.models.Customer.Customer;
import com.crudSE.demo.models.OrderList;
import com.crudSE.demo.service.CustomerService;
import com.crudSE.demo.service.OrderListService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {
  
  private final CustomerService customerService;
  private final OrderListService orderListService;
  
  public CustomerController( CustomerService customerService, OrderListService orderListService) {
    this.customerService = customerService;
    this.orderListService = orderListService;
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
  
  
//  OrderList added on customer controller itself
  @PostMapping("/orderList/create")
  public OrderList createOrderList(@RequestBody OrderList orderList){
    return this.orderListService.createOrderList(orderList);
  }
  
  @GetMapping("/orderList/{id}")
  public OrderList getOrderListById(@PathVariable Long id){
    return this.orderListService.getOrderListById(id);
  }
  
//
//
//  @GetMapping("/all")
//  public List<OrderList> getAllOrderList(){
//    return this.orderListService.getAllOrderLists();
//  }
//
//
//  @PostMapping("/update")
//  public OrderList updateOrderList(@RequestBody OrderList orderList){
//    return this.orderListService.updateOrderList(orderList);
//  }
  
//  @DeleteMapping
//  public ResponseEntity<String> deleteOrderList(@RequestBody OrderList orderList){
//    return ResponseEntity.ok(this.orderListService.deleteOrderList(orderList));
//  }
}
