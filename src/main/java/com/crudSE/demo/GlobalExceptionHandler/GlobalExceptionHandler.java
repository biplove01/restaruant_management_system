package com.crudSE.demo.GlobalExceptionHandler;

import com.crudSE.demo.GlobalExceptionHandler.CustomExceptions.AlreadyExistsException;
import com.crudSE.demo.GlobalExceptionHandler.CustomExceptions.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
  
  @ExceptionHandler(AlreadyExistsException.class)
  public ResponseEntity<Map<String, String>> handleAlreadyExists(AlreadyExistsException exception) {
    return ResponseEntity.badRequest().body(Map.of("error", exception.getMessage()));
  }
  
  //  @ExceptionHandler(ResourceNotFoundException.class)
//  public ResponseEntity<Map<String ,String>> handleResourceNotFound (ResourceNotFoundException exception){
//    return ResponseEntity.badRequest().body(Map.of("error", exception.getMessage()));
//  }
  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<Map<String, String>> handleResourceNotFound(ResourceNotFoundException ex) {
    Map<String, String> error = new HashMap<>();
    error.put("error", ex.getMessage());  // same string behavior
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);  // now 404
  }
  
}
