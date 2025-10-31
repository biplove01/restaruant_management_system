package com.crudSE.demo.controller;

import com.crudSE.demo.models.MenuItem;
import com.crudSE.demo.service.MenuItemService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menuItem")
public class MenuItemController {

  private final MenuItemService menuItemService;
  
  public MenuItemController(MenuItemService menuItemService) {
    this.menuItemService = menuItemService;
  }
  
  @PostMapping("/create")
  public MenuItem createMenuItem(@RequestBody MenuItem menuItem){
    return this.menuItemService.createMenuItem(menuItem);
  }
  
  @GetMapping("/{id}")
  public MenuItem getMenuItemById(@PathVariable Long id){
    return this.menuItemService.getMenuItemById(id);
  }
  
  
  @GetMapping("/all")
  public List<MenuItem> getAllMenuItem(){
    return this.menuItemService.getAllMenuItems();
  }
  
  
  @PostMapping("/update")
  public MenuItem updateMenuItem(@RequestBody MenuItem menuItem){
    return this.menuItemService.updateMenuItem(menuItem);
  }
  
  @DeleteMapping
  public ResponseEntity<String> deleteMenuItem (@RequestBody MenuItem menuItem){
    return ResponseEntity.ok(this.menuItemService.deleteMenuItem(menuItem));
  }
  
}
