package com.crudSE.demo.service;

import com.crudSE.demo.GlobalExceptionHandler.CustomExceptions.ResourceNotFoundException;
import com.crudSE.demo.models.Customer.Customer;
import com.crudSE.demo.models.OrderItem;
import com.crudSE.demo.models.OrderList;
import com.crudSE.demo.repositories.CustomerRepository;
import com.crudSE.demo.repositories.OrderListRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderListService {
  
  private final OrderListRepository orderListRepository;
  private final CustomerRepository customerRepository;
  
  public OrderListService(OrderListRepository orderListRepository, CustomerRepository customerRepository) {
    this.orderListRepository = orderListRepository;
    this.customerRepository = customerRepository;
  }
  
  public OrderList createOrderList( OrderList orderList) {
    
    Customer customer = this.customerRepository.findById(orderList.getCustomer().getId()).orElseThrow(()-> new ResourceNotFoundException("User of id: " + orderList.getCustomer().getId() + " does not exists"));
    
    orderList.setCustomer(customer);
    
    for (OrderItem item: orderList.getOrderItems()){
      item.setOrderList(orderList);
    }
    
    return this.orderListRepository.save(orderList);
  }
  
//  public OrderList createOrderListByTable(OrderList orderList)
  
  public OrderList getOrderListById(Long id) {
    return this.orderListRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Order list of id: " + id + " does not exists"));
  }
  
  
  public List<OrderList> getAllOrderLists() {
    return this.orderListRepository.findAll();
  }
  
  
  public OrderList updateOrderList( OrderList updatedOrderList) {
    
    OrderList existingOrderList = this.orderListRepository.findById(updatedOrderList.getId()).orElseThrow(()-> new ResourceNotFoundException("Order list of id: " + updatedOrderList.getId() + " is not found!"));
    
    if(updatedOrderList.getCustomer() != null){
      Customer customer = this.customerRepository.findById(updatedOrderList.getCustomer().getId()).orElseThrow(()-> new ResourceNotFoundException("Customer of id: " + updatedOrderList.getCustomer().getId() + " does not exists!" ));
      existingOrderList.setCustomer(customer);
    }

    existingOrderList.getOrderItems().clear();
    
    for(OrderItem item: updatedOrderList.getOrderItems()){
      item.setOrderList(existingOrderList);
      existingOrderList.getOrderItems().add(item);
    }
    
    return this.orderListRepository.save(existingOrderList);
  }
  
  public String deleteOrderList(OrderList orderList) {
    if(!this.orderListRepository.existsById(orderList.getId())){
      throw new ResourceNotFoundException("Order list of id: " + orderList.getId() + "does not exists");
    }
    
    this.orderListRepository.delete(orderList);
    return "Order list of id: " + orderList.getId() + " successfully deleted";
  }
}
