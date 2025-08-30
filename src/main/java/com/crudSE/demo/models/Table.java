package com.crudSE.demo.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class Table {
  
@Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @OneToMany
  @JoinColumn(name = "table_customers")
  private List<OrderItem> orderItems = new ArrayList<>();
}
