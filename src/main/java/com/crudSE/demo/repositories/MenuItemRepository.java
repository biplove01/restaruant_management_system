package com.crudSE.demo.repositories;

import com.crudSE.demo.models.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
  boolean existsByName(String name);
  
  Optional<MenuItem> findByName(String name);
}
