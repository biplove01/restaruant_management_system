package com.crudSE.demo.controller;

import com.crudSE.demo.models.OrderList;
import com.crudSE.demo.service.OrderListService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orderList")
public class OrderListController {
  
  private final OrderListService orderListService;
  
  public OrderListController(OrderListService orderListService) {
    this.orderListService = orderListService;
  }
  
  
  @PostMapping("/create")
  public OrderList createOrderList( @RequestBody OrderList orderList){
    return this.orderListService.createOrderList(orderList);
  }
  
  @GetMapping("/{id}")
  public OrderList getOrderListById(@PathVariable Long id){
    return this.orderListService.getOrderListById(id);
  }
  
  
  @GetMapping("/all")
  public List<OrderList> getAllOrderList(){
    return this.orderListService.getAllOrderLists();
  }
  
  
  @PostMapping("/update")
  public OrderList updateOrderList(@RequestBody OrderList orderList){
    return this.orderListService.updateOrderList(orderList);
  }
  
  @DeleteMapping("{id}")
  public ResponseEntity<String> deleteOrderList(@PathVariable Long id){
    return ResponseEntity.ok(this.orderListService.deleteOrderList(id));
  }
  
}
