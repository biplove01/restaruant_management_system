# Restaurant Management System - Documentation

## Table of Contents
1. [Project Overview](#project-overview)
2. [Features](#features)
3. [Architecture](#architecture)
4. [Technology Stack](#technology-stack)
5. [Project Structure](#project-structure)
6. [Database Schema](#database-schema)
7. [API Endpoints](#api-endpoints)
8. [Models](#models)
9. [Services & Repositories](#services--repositories)
10. [Exception Handling](#exception-handling)
11. [Testing](#testing)
12. [Setup & Configuration](#setup--configuration)

---

## Project Overview

A Spring Boot REST API for managing restaurant operations including employees, menu items, orders, and table reservations. Built with Spring Data JPA, MySQL, and comprehensive test coverage.

---

## Features

- **Employee Management**: CRUD operations with role-based access and email validation
- **Menu Item Management**: CRUD with categorization (Breakfast, Lunch, Hot/Cold Drinks, Alcohol)
- **Order Management**: Transactional order processing with status tracking
- **Table Management**: Table reservation with order association
- **Exception Handling**: Global exception handler with custom errors

---

## Architecture

**Design Patterns**: MVC, Repository Pattern, DTO Pattern, Global Exception Handling

```
Controllers → Services → Repositories → Database
```

**Layer Responsibilities**:
- **Controllers**: REST endpoints, request/response handling
- **Services**: Business logic, validation, transaction management
- **Repositories**: Data access using Spring Data JPA

---

## Technology Stack

- Spring Boot 3.3.4
- Java 17
- MySQL 8.0
- Spring Data JPA / Hibernate
- Maven
- JUnit 5, Mockito
- SpringDoc OpenAPI (Swagger)
- Lombok

---

## Project Structure

```
src/main/java/com/crudSE/demo/
├── controller/          # REST controllers
├── service/             # Business logic
├── repositories/        # Data access
├── models/              # Entities and enums
├── DTOs/                # Data Transfer Objects
└── GlobalExceptionHandler/  # Exception handling

src/test/java/com/crudSE/demo/
├── controller/          # Controller tests
└── service/             # Service tests
```

---

## Database Schema

### Entity Relationships

```
Employee ──┬──> Role (Many-to-Many)
           │
OrderList ─┼──> Table (Many-to-One)
           │
OrderItem ─┴──> OrderList (Many-to-One)
           │
           └──> MenuItem (Many-to-One)
```

### Key Tables

| Table | Key Fields | Relationships |
|-------|------------|---------------|
| `employee` | id, name, email (unique), phone, address, password | Many-to-Many with Role |
| `role` | id, name | Many-to-Many with Employee |
| `menu_item` | id, name (unique), price, category | One-to-Many with OrderItem |
| `restaurant_table` | id, table_number (unique) | One-to-Many with OrderList |
| `order_list` | id, table_id | Many-to-One with Table, One-to-Many with OrderItem |
| `order_item` | id, quantity, order_status, menu_item_id, order_list_id | Many-to-One with MenuItem, Many-to-One with OrderList |

### Enums

- **MenuItemCategory**: BREAKFAST, LUNCH, HOT_DRINK, COLD_DRINK, ALCOHOL
- **OrderStatus**: INITIAL, PENDING, READY, DELIVERED
- **RoleStatus**: OWNER, MANAGER, CASHIER, WAITER, CHEF

---

## API Endpoints

**Base URL**: `http://localhost:8080`

### Employee Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/employee/create` | Create employee |
| GET | `/api/employee/{id}` | Get employee by ID |
| GET | `/api/employee/all` | Get all employees |
| POST | `/api/employee/update` | Update employee |
| DELETE | `/api/employee/{id}` | Delete employee |

**Example Request (Create)**:
```json
{
  "name": "John Doe",
  "email": "john.doe@example.com",
  "address": "123 Main St",
  "phone": 1234567890,
  "password": "password123",
  "roles": []
}
```

**Example Response**:
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john.doe@example.com",
  "address": "123 Main St",
  "phone": 1234567890
}
```

**Error Response (400/404)**:
```json
{
  "error": "Error message"
}
```

---

### Menu Item Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/menuItem/create` | Create menu item |
| GET | `/api/menuItem/{id}` | Get menu item by ID |
| GET | `/api/menuItem/all` | Get all menu items |
| POST | `/api/menuItem/update` | Update menu item |
| DELETE | `/api/menuItem` | Delete menu item (body required) |

**Example Request (Create)**:
```json
{
  "name": "Burger",
  "price": 12.99,
  "category": "LUNCH"
}
```

---

### Order List Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/orderList/create` | Create order (transactional) |
| GET | `/api/orderList/{id}` | Get order by ID |
| GET | `/api/orderList/all` | Get all orders |
| POST | `/api/orderList/update` | Update order |
| DELETE | `/api/orderList/{id}` | Delete order |

**Example Request (Create)**:
```json
{
  "table": { "id": 1 },
  "orderItems": [
    {
      "quantity": 2,
      "orderStatus": "PENDING",
      "menuItem": { "id": 1 }
    }
  ]
}
```

---

### Table Management

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/table/create` | Create table |
| GET | `/api/table/{id}` | Get table by ID |
| GET | `/api/table/all` | Get all tables |
| POST | `/api/table/update/{id}` | Update table |
| DELETE | `/api/table/{id}` | Delete table |

**Example Request (Create)**:
```json
{
  "tableNumber": "T1"
}
```

---

## Models

### Employee
```java
@Entity
public class Employee {
    private Long id;
    private String name;
    private String email;        // unique
    private String address;
    private Long phone;
    private String password;
    private List<Role> roles;    // Many-to-Many
}
```

### MenuItem
```java
@Entity
public class MenuItem {
    private Long id;
    private String name;         // unique
    private Float price;
    private MenuItemCategory category;
}
```

### Table
```java
@Entity
@Table(name = "restaurant_table")
public class Table {
    private Long id;
    private String tableNumber;   // unique
    private List<OrderList> orderList;
}
```

### OrderList
```java
@Entity
public class OrderList {
    private Long id;
    private List<OrderItem> orderItems;
    private Table table;          // Many-to-One
}
```

### OrderItem
```java
@Entity
public class OrderItem {
    private Long id;
    private Integer quantity;
    private OrderStatus orderStatus;
    private MenuItem menuItem;    // Many-to-One
    private OrderList orderList;   // Many-to-One
}
```

### Role
```java
@Entity
public class Role {
    private Long id;
    private String name;
}
```

---

## Services & Repositories

### EmployeeService
- `createEmployee(Employee)`: EmployeeDTO - validates email uniqueness
- `getEmployeeById(Long)`: EmployeeDTO
- `getAllEmployees()`: List<EmployeeDTO>
- `updateEmployee(EmployeeDTO)`: EmployeeDTO
- `deleteEmployee(Long)`: String

**EmployeeRepository**:
- `existsByEmail(String)`: boolean
- `findByEmail(String)`: Optional<Employee>

---

### MenuItemService
- `createMenuItem(MenuItem)`: MenuItem - validates name uniqueness
- `getMenuItemById(Long)`: MenuItem
- `getMenuItemByName(String)`: MenuItem
- `getAllMenuItems()`: List<MenuItem>
- `updateMenuItem(MenuItem)`: MenuItem
- `deleteMenuItem(MenuItem)`: String

**MenuItemRepository**:
- `existsByName(String)`: boolean
- `findByName(String)`: Optional<MenuItem>

---

### OrderListService
- `createOrderList(OrderList)`: OrderList - **@Transactional**, validates table
- `getOrderListById(Long)`: OrderList
- `getAllOrderLists()`: List<OrderList>
- `updateOrderList(OrderList)`: OrderList
- `deleteOrderList(Long)`: String

**OrderListRepository**: Standard JPA methods

**TableRepository**:
- `existsByTableNumber(String)`: boolean

---

### TableService
- `createTable(Table)`: Table - validates table number uniqueness
- `getTableById(Long)`: Table
- `getAllTables()`: List<Table>
- `updateTable(Long, Table)`: Table
- `deleteTable(Long)`: String

**TableRepository**:
- `existsByTableNumber(String)`: boolean

---

## Exception Handling

### GlobalExceptionHandler
Centralized exception handling using `@ControllerAdvice`:

- **AlreadyExistsException** → `400 Bad Request`
  - Used for: duplicate email, duplicate menu item name, duplicate table number
- **ResourceNotFoundException** → `404 Not Found`
  - Used for: entity not found by ID or name

**Error Response Format**:
```json
{
  "error": "Error message"
}
```

---

## Testing

### Test Structure
- **Controller Tests**: 4 test files covering all REST endpoints
- **Service Tests**: 4 test files covering all business logic

### Test Coverage
- **Total Test Files**: 8
- **Total Test Methods**: ~77
- **Framework**: JUnit 5, Mockito, Spring Test, MockMvc

### Test Scenarios
- Success paths for all CRUD operations
- Exception scenarios (404, 400)
- Edge cases (empty lists, null handling)
- HTTP status code validation
- JSON response validation

### Running Tests
```bash
mvn test                    # Run all tests
mvn test -Dtest=ClassName  # Run specific test class
```

---

## Setup & Configuration

### Prerequisites
- Java 17+
- Maven 3.6+
- MySQL 8.0+

### Setup Steps

1. **Create Database**:
```sql
CREATE DATABASE restaurantManagementDB;
```

2. **Configure** `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/restaurantManagementDB
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

3. **Build and Run**:
```bash
mvn clean compile
mvn spring-boot:run
```

4. **Access**:
- API Base: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

### Key Configuration

- `spring.jpa.hibernate.ddl-auto=update`: Updates schema on startup
- `spring.jpa.show-sql=true`: Enables SQL logging

---

## Additional Resources

- **Swagger UI**: Interactive API documentation and testing
- **Lombok**: Reduces boilerplate code (@Data, @Getter, @Setter)
- **Spring Data JPA**: Automatic repository implementation
- **Global Exception Handler**: Centralized error responses

---

**Version**: 1.0.0
**Last Updated**: 2024
