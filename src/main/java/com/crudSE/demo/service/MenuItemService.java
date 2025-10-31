package com.crudSE.demo.service;

import com.crudSE.demo.GlobalExceptionHandler.CustomExceptions.AlreadyExistsException;
import com.crudSE.demo.GlobalExceptionHandler.CustomExceptions.ResourceNotFoundException;
import com.crudSE.demo.models.MenuItem;
import com.crudSE.demo.repositories.MenuItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuItemService {
  
  private final MenuItemRepository menuItemRepository;
  
  public MenuItemService(MenuItemRepository menuItemRepository) {
    this.menuItemRepository = menuItemRepository;
  }
  
  
  
  public MenuItem createMenuItem(MenuItem menuItem) {
    if(this.menuItemRepository.existsByName(menuItem.getName())){
      throw new AlreadyExistsException("Menu item with the name: " + menuItem.getName() + " already exists!");
    }
    return this.menuItemRepository.save(menuItem);
  }
  
  
  
  public MenuItem getMenuItemById(Long id) {
    return this.menuItemRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Menu item with id: " + id + " does not exists"));
  }
  
  
  
  public MenuItem getMenuItemByName(String name) {
    return this.menuItemRepository.findByName(name).orElseThrow(()-> new ResourceNotFoundException("Menu item with name: " + name + " does not exists"));
  }
  
  
  
  public List<MenuItem> getAllMenuItems() {
    return this.menuItemRepository.findAll();
  }
  
  
  
  public MenuItem updateMenuItem(MenuItem menuItem) {
    
    if(!this.menuItemRepository.existsByName(menuItem.getName()) || !this.menuItemRepository.existsById(menuItem.getId())){
      throw new ResourceNotFoundException("No such menu item exists with name: "+ menuItem.getName() + " and id: "+ menuItem.getId());
    }
    
    MenuItem existingMenuItem = this.menuItemRepository.findByName(menuItem.getName()).orElseThrow(()-> new ResourceNotFoundException("No such menu item exists with name: "+ menuItem.getName() + " and id: "+ menuItem.getId()));
    
    existingMenuItem.setName(menuItem.getName());
    existingMenuItem.setCategory(menuItem.getCategory());
    existingMenuItem.setPrice(menuItem.getPrice());

    return this.menuItemRepository.save(existingMenuItem);
  }
  
  
  
  public String deleteMenuItem(MenuItem menuItem) {
    
    if (!this.menuItemRepository.existsById(menuItem.getId())) {
      throw new ResourceNotFoundException("No such menu item exists with id: " + menuItem.getId());
    }
    
    this.menuItemRepository.deleteById(menuItem.getId());
    
    return "Menu item successfully deleted";
  }
  
}
