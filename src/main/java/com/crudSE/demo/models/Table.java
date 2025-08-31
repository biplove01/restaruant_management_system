package com.crudSE.demo.models;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@jakarta.persistence.Table(name = "restaurant_table")
public class Table {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  
  private String tableNumber;
  
  @OneToOne
  @JoinColumn(name = "table_orderList")
  private OrderList orderList;
}
