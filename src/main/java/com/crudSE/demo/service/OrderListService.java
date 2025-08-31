package com.crudSE.demo.service;

import com.crudSE.demo.GlobalExceptionHandler.CustomExceptions.ResourceNotFoundException;
import com.crudSE.demo.models.OrderList;
import com.crudSE.demo.models.OrderItem;
import com.crudSE.demo.models.Table;
import com.crudSE.demo.repositories.OrderListRepository;
import com.crudSE.demo.repositories.TableRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderListService {
  
  private final OrderListRepository orderListRepository;
  private final TableRepository tableRepository;
  
  public OrderListService(OrderListRepository orderListRepository, TableRepository tableRepository) {
    this.orderListRepository = orderListRepository;
    this.tableRepository = tableRepository;
  }
  
  public OrderList createOrderList(OrderList orderList) {
    Table table = this.tableRepository.findById(orderList.getTable().getId())
        .orElseThrow(() -> new ResourceNotFoundException("Table of id: " + orderList.getTable().getId() + " does not exist"));
    
    orderList.setTable(table);
    
    for (OrderItem item : orderList.getOrderItems()) {
      item.setOrderList(orderList);
    }
    
    return this.orderListRepository.save(orderList);
  }
  
  public OrderList getOrderListById(Long id) {
    return this.orderListRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Order list of id: " + id + " does not exist"));
  }
  
  public List<OrderList> getAllOrderLists() {
    return this.orderListRepository.findAll();
  }
  
  public OrderList updateOrderList(OrderList updatedOrderList) {
    OrderList existingOrderList = this.orderListRepository.findById(updatedOrderList.getId())
        .orElseThrow(() -> new ResourceNotFoundException("Order list of id: " + updatedOrderList.getId() + " is not found!"));
    
    if (updatedOrderList.getTable() != null) {
      Table table = this.tableRepository.findById(updatedOrderList.getTable().getId())
          .orElseThrow(() -> new ResourceNotFoundException("Table of id: " + updatedOrderList.getTable().getId() + " does not exist!"));
      existingOrderList.setTable(table);
    }
    
    existingOrderList.getOrderItems().clear();
    
    for (OrderItem item : updatedOrderList.getOrderItems()) {
      item.setOrderList(existingOrderList);
      existingOrderList.getOrderItems().add(item);
    }
    
    return this.orderListRepository.save(existingOrderList);
  }
  
  public String deleteOrderList(OrderList orderList) {
    if (!this.orderListRepository.existsById(orderList.getId())) {
      throw new ResourceNotFoundException("Order list of id: " + orderList.getId() + " does not exist");
    }
    
    this.orderListRepository.delete(orderList);
    return "Order list of id: " + orderList.getId() + " successfully deleted";
  }
}
