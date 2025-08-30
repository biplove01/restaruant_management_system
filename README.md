# Restaurant Management System

A comprehensive Spring Boot-based restaurant management system designed to handle customer management, menu items, orders, employee management, and table reservations. This project was built for the Software Engineering subject, demonstrating modern web application development practices.

## Features

### Core Functionality
- **Customer Management**: Complete CRUD operations for customer profiles
- **Employee Management**: Staff management with role-based access control
- **Menu Management**: Comprehensive menu item management with categorization
- **Order Management**: Complete order lifecycle management
- **Table Management**: Restaurant table reservation and management system
- **Role-based Access Control**: Secure access management for different user types

### Technical Features
- **RESTful API**: Clean, RESTful endpoints for all operations
- **Database Integration**: MySQL database with JPA/Hibernate ORM
- **Exception Handling**: Global exception handling with custom error responses
- **Data Validation**: Input validation and error handling
- **Lombok Integration**: Reduced boilerplate code for cleaner models

## 🏗️ Architecture

### Technology Stack
- **Backend Framework**: Spring Boot 3.5.4
- **Java Version**: Java 17
- **Database**: MySQL 8.0
- **ORM**: Spring Data JPA with Hibernate
- **Build Tool**: Maven
- **Additional Libraries**: Lombok for code reduction

### Project Structure
```
src/main/java/com/crudSE/demo/
├── controller/          # REST API controllers
├── service/            # Business logic layer
├── repositories/       # Data access layer
├── models/            # Entity models and DTOs
├── DTOs/              # Data Transfer Objects
├── GlobalExceptionHandler/  # Exception handling
└── CrudAppSeApplication.java  # Main application class
```

### Design Patterns
- **MVC Architecture**: Separation of concerns between layers
- **Repository Pattern**: Data access abstraction
- **Service Layer**: Business logic encapsulation
- **DTO Pattern**: Data transfer optimization
- **Global Exception Handling**: Centralized error management

## 📋 Prerequisites

Before running this application, ensure you have the following installed:

- **Java 17** or higher
- **Maven 3.6+**
- **MySQL 8.0+**
- **Git** (for version control)

## 🛠️ Installation & Setup

### 1. Clone the Repository
```bash
git clone <repository-url>
cd restaurant_management_system
```

### 2. Database Setup
1. Create a MySQL database:
```sql
CREATE DATABASE restaurantManagementDB;
```

2. Update database configuration in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://127.0.0.1:3306/restaurantManagementDB
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Build and Run
```bash
# Clean and compile
mvn clean compile

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 🔌 API Endpoints

### Customer Management
- `POST /api/customer/create` - Create a new customer
- `GET /api/customer/{id}` - Get customer by ID
- `GET /api/customer/all` - Get all customers
- `POST /api/customer/update` - Update customer information
- `DELETE /api/customer` - Delete customer

### Employee Management
- `POST /api/employee/create` - Create a new employee
- `GET /api/employee/{id}` - Get employee by ID
- `GET /api/employee/all` - Get all employees
- `POST /api/employee/update` - Update employee information
- `DELETE /api/employee` - Delete employee

### Menu Item Management
- `POST /api/menu-item/create` - Create a new menu item
- `GET /api/menu-item/{id}` - Get menu item by ID
- `GET /api/menu-item/all` - Get all menu items
- `POST /api/menu-item/update` - Update menu item
- `DELETE /api/menu-item` - Delete menu item

### Order Management
- `POST /api/order/create` - Create a new order
- `GET /api/order/{id}` - Get order by ID
- `GET /api/order/all` - Get all orders
- `POST /api/order/update` - Update order
- `DELETE /api/order` - Delete order

## 🗄️ Database Schema

### Core Entities
- **Customer**: Customer profiles with contact information
- **Employee**: Staff members with role assignments
- **MenuItem**: Food and beverage items with pricing
- **OrderList**: Customer orders with items
- **OrderItem**: Individual items within orders
- **Table**: Restaurant table management
- **Role**: Employee role definitions

### Relationships
- Customer → OrderList (One-to-Many)
- Employee → Role (Many-to-One)
- OrderList → OrderItem (One-to-Many)
- MenuItem → OrderItem (One-to-Many)

## 🧪 Testing

Run the test suite using Maven:
```bash
mvn test
```

## 📦 Build

### Create JAR File
```bash
mvn clean package
```

### Create Docker Image
```bash
mvn spring-boot:build-image
```

## 🔧 Configuration

### Application Properties
Key configuration options in `application.properties`:
- Database connection settings
- JPA/Hibernate configuration
- Logging levels
- Server port configuration

### Environment Variables
The application supports environment variable overrides for:
- Database credentials
- Server configuration
- Logging levels

## 🚀 Deployment

### Local Development
```bash
mvn spring-boot:run
```

### Production Deployment
1. Build the application: `mvn clean package`
2. Run the JAR: `java -jar target/demo-0.0.1-SNAPSHOT.jar`

### Docker Deployment
```bash
docker run -p 8080:8080 restaurant-management-system:latest
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch: `git checkout -b feature-name`
3. Commit your changes: `git commit -am 'Add feature'`
4. Push to the branch: `git push origin feature-name`
5. Submit a pull request

## 📝 License

This project is developed for educational purposes as part of the Software Engineering curriculum.

## 👥 Team

This project was developed as part of the Software Engineering course requirements.

## 📞 Support

For questions or support, please refer to the course instructor or create an issue in the repository.

## 🔄 Version History

- **v0.0.1-SNAPSHOT**: Initial development version with core CRUD operations

## 📚 Additional Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA Reference](https://docs.spring.io/spring-data/jpa/docs/current/reference/html/)
- [MySQL Documentation](https://dev.mysql.com/doc/)
- [Maven Documentation](https://maven.apache.org/guides/)

---

**Note**: This is a development version. For production use, additional security measures, comprehensive testing, and performance optimizations should be implemented.
