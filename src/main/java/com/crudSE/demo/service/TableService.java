package com.crudSE.demo.service;

import com.crudSE.demo.GlobalExceptionHandler.CustomExceptions.AlreadyExistsException;
import com.crudSE.demo.GlobalExceptionHandler.CustomExceptions.ResourceNotFoundException;
import com.crudSE.demo.models.Table;
import com.crudSE.demo.repositories.TableRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TableService {
  
  private final TableRepository tableRepository;
  
  public TableService(TableRepository tableRepository) {
    this.tableRepository = tableRepository;
  }
  
  public Table createTable(Table table){
    if(this.tableRepository.existsByTableNumber((table.getTableNumber()))){
      throw new AlreadyExistsException("The table with number: " + table.getTableNumber() + " already exists");
    }
    return this.tableRepository.save(table);
  }
  
  public Table getTableById(Long id) {
    return this.tableRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("The table does not exist with id: "+ id ));
  }
  
  public List<Table> getAllTables() {
    return this.tableRepository.findAll();
  }
  
  public String deleteTable(Long id) {
    if(!this.tableRepository.existsById(id)){
      throw new ResourceNotFoundException("Table with id: " + id + " does not exist");
    }
    this.tableRepository.deleteById(id);
    return "Table with id " + id + " successfully deleted";
  }
  
  public Table updateTable(Long id, Table table) {
    Table tableToUpdate = this.tableRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("The table with id "+ id + " is not found"));

    tableToUpdate.setTableNumber(table.getTableNumber());
//    tableToUpdate.setMenuItems(table.getMenuItems());

    return this.tableRepository.save(tableToUpdate);
  }
}
