package com.crudSE.demo.repositories;

import com.crudSE.demo.models.Table;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TableRepository extends JpaRepository<Table, Long> {
  
  
  boolean existsByTableNumber(String tableNumber);
}
