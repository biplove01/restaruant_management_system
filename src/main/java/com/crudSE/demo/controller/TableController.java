package com.crudSE.demo.controller;

import com.crudSE.demo.models.OrderList;
import com.crudSE.demo.models.Table;
import com.crudSE.demo.service.OrderListService;
import com.crudSE.demo.service.TableService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/table")
public class TableController {
  
  private final TableService tableService;
  private final OrderListService orderListService;
  
  public TableController(TableService tableService, OrderListService orderListService) {
    this.tableService = tableService;
    this.orderListService = orderListService;
  }
  
  @PostMapping("/create")
  public Table createTable(@RequestBody Table table){
    return this.tableService.createTable(table);
  }
  
  @GetMapping("/{id}")
  public Table getTableById(@PathVariable Long id){
    return this.tableService.getTableById(id);
  }
  
  @GetMapping("/all")
  public List<Table> getAllTables(){
    return this.tableService.getAllTables();
  }
  
  @DeleteMapping("/{id}")
  public ResponseEntity<String> deleteTable(@PathVariable Long id){
    return ResponseEntity.ok(this.tableService.deleteTable(id));
  }
  
  @PostMapping("/update/{id}")
  public Table updateTable(@PathVariable Long id, @RequestBody Table table){
    return this.tableService.updateTable(id, table);
  }
  
  
//  @PostMapping("/orderList/create")
//  public OrderList createOrderList(@RequestBody OrderList orderList){
//    return this.orderListService.createOrderList(orderList);
//  }
//
//  @GetMapping("/orderList/{id}")
//  public OrderList getOrderListById(@PathVariable Long id){
//    return this.orderListService.getOrderListById(id);
//  }

}
