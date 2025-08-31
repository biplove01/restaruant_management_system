package com.crudSE.demo.models;


import com.crudSE.demo.models.enums.OrderStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class OrderItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  
  private Integer quantity;
  private OrderStatus orderStatus;
  
  @ManyToOne
  @JoinColumn(name = "menu_item_id")
  private MenuItem menuItem;
  
  @ManyToOne
  @JoinColumn(name="order_list_id")
  @JsonBackReference
  private OrderList orderList;
}
