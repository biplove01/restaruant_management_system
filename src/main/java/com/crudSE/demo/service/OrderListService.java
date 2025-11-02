package com.crudSE.demo.service;

import com.crudSE.demo.GlobalExceptionHandler.CustomExceptions.ResourceNotFoundException;
import com.crudSE.demo.models.MenuItem;
import com.crudSE.demo.models.OrderList;
import com.crudSE.demo.models.OrderItem;
import com.crudSE.demo.models.Table;
import com.crudSE.demo.repositories.MenuItemRepository;
import com.crudSE.demo.repositories.OrderListRepository;
import com.crudSE.demo.repositories.TableRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderListService {

  private final OrderListRepository orderListRepository;
  private final TableRepository tableRepository;
  private final MenuItemRepository menuItemRepository;


  public OrderListService(OrderListRepository orderListRepository, TableRepository tableRepository, MenuItemRepository menuItemRepository) {
    this.orderListRepository = orderListRepository;
    this.tableRepository = tableRepository;
    this.menuItemRepository = menuItemRepository;
  }

  @Transactional
  public OrderList createOrderList(OrderList orderList) {

    if (orderList.getTable() == null || orderList.getTable().getId() == null) {
      throw new IllegalArgumentException("Table information is missing in the request");
    }

    Table table = this.tableRepository.findById(orderList.getTable().getId())
        .orElseThrow(() -> new ResourceNotFoundException("Table with ID: " + orderList.getTable().getId() + " does not exist"));

    orderList.setTable(table);

    if (orderList.getOrderItems() != null) {
      for (OrderItem item : orderList.getOrderItems()) {
        MenuItem menuItem = menuItemRepository.findById(item.getMenuItem().getId())
            .orElseThrow(() -> new ResourceNotFoundException("Menu item with ID: " + item.getMenuItem().getId() + " not found"));

        item.setMenuItem(menuItem);
        item.setOrderList(orderList);
      }
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


  public String deleteOrderList(Long id) {
    if (!this.orderListRepository.existsById(id)) {
      throw new ResourceNotFoundException("Order list of id: " + id + " does not exist");
    }

    this.orderListRepository.deleteById(id);
    return "Order list of id: " + id + " successfully deleted";
  }
}
