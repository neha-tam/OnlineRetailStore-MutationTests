package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {

    @Test
    void testConstructor() {
        // Arrange
        Integer expectedCustomerID = 1;
        String expectedCustomerName = "John Doe";

        // Act
        Customer customer = new Customer(expectedCustomerID, expectedCustomerName);

        // Assert
        assertEquals(expectedCustomerID, customer.getCustomerID(), "Customer ID should match the value set in the constructor.");
        assertEquals(expectedCustomerName, customer.getCustomerName(), "Customer name should match the value set in the constructor.");
    }

    @Test
    void testSetAndGetCustomerID() {
        // Arrange
        Customer customer = new Customer(0, "Jane Doe");
        Integer expectedCustomerID = 42;

        // Act
        customer.setCustomerID(expectedCustomerID);
        Integer actualCustomerID = customer.getCustomerID();

        // Assert
        assertEquals(expectedCustomerID, actualCustomerID, "Customer ID should match the value set.");
    }

    @Test
    void testSetAndGetCustomerName() {
        // Arrange
        Customer customer = new Customer(0, "Jane Doe");
        String expectedCustomerName = "Alice";

        // Act
        customer.setCustomerName(expectedCustomerName);
        String actualCustomerName = customer.getCustomerName();

        // Assert
        assertEquals(expectedCustomerName, actualCustomerName, "Customer name should match the value set.");
    }

    @Test
    void testSetCustomerID_Null() {
        // Arrange
        Customer customer = new Customer(42, "John Doe");

        // Act
        customer.setCustomerID(null);
        Integer actualCustomerID = customer.getCustomerID();

        // Assert
        assertNull(actualCustomerID, "Customer ID should be null after setting a null value.");
    }

    @Test
    void testSetCustomerName_Null() {
        // Arrange
        Customer customer = new Customer(42, "John Doe");

        // Act
        customer.setCustomerName(null);
        String actualCustomerName = customer.getCustomerName();

        // Assert
        assertNull(actualCustomerName, "Customer name should be null after setting a null value.");
    }

    @Test
    void testDefaultValues() {
        // Arrange & Act
        Customer customer = new Customer(0, null);

        // Assert
        assertEquals(0, customer.getCustomerID(), "Default Customer ID should match the constructor value.");
        assertNull(customer.getCustomerName(), "Customer name should be null if not explicitly set.");
    }
}
