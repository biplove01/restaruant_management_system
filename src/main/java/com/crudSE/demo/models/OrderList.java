package com.crudSE.demo.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Entity
@Data
public class OrderList {
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  @OneToMany(mappedBy = "orderList", cascade = CascadeType.ALL)
  @JsonManagedReference
  private List<OrderItem> orderItems;
  
  @ManyToOne
  @JoinColumn(name = "table_id")
  @JsonBackReference
  private Table table;
}
