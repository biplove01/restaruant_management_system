package com.crudSE.demo.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;


@Data
@Entity
@jakarta.persistence.Table(name = "restaurant_table")
public class Table {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  
  private String tableNumber;
  
  @OneToMany(mappedBy = "table", cascade = CascadeType.ALL)
  @JsonManagedReference
  private List<OrderList> orderList;
}
