package com.crudSE.demo.models;

import com.crudSE.demo.models.Customer.Customer;
import com.crudSE.demo.models.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class OrderList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private float totalAmount;
    private OrderStatus status;
    private LocalDateTime orderDate;
}
