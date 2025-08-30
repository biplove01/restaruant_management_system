package com.crudSE.demo.models.Customer;

import com.crudSE.demo.models.OrderList;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Customer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String name;
  private String email;
  private String password;
  private String address;
  private Long phone;
  
  @OneToMany(mappedBy = "customer")
  private List<OrderList> orderList;

}
