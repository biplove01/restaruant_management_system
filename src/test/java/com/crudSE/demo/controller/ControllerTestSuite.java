package com.crudSE.demo.controller;

import org.junit.platform.suite.api.SelectClasses;
import org.junit.platform.suite.api.Suite;
import org.junit.platform.suite.api.SuiteDisplayName;

/**
 * Test Suite for all Controller classes in the Restaurant Management System.
 *
 * This suite includes comprehensive tests for:
 * - CustomerController: Customer CRUD operations
 * - EmployeeController: Employee CRUD operations
 * - MenuItemController: Menu item CRUD operations
 * - OrderListController: Order management operations
 *
 * Test Coverage:
 * - Positive test cases (success scenarios)
 * - Negative test cases (error scenarios)
 * - Edge cases (null values, empty data, boundary conditions)
 * - Service layer integration verification
 * - HTTP response validation
 * - JSON serialization/deserialization
 */
@Suite
@SuiteDisplayName("Restaurant Management System Controller Test Suite")
@SelectClasses({
    CustomerControllerTest.class,
    EmployeeControllerTest.class,
    MenuItemControllerTest.class,
    OrderListControllerTest.class
})
public class ControllerTestSuite {
    // This class serves as a test suite container
    // All tests are defined in the individual test classes
}
