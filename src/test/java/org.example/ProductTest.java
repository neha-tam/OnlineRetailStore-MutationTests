package org.example;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void testSetAndGetUniqueID() {
        // Arrange
        Product product = new Product();
        Integer expectedUniqueID = 123;

        // Act
        product.setUniqueID(expectedUniqueID);
        Integer actualUniqueID = product.getUniqueID();

        // Assert
        assertEquals(expectedUniqueID, actualUniqueID, "The unique ID should match the value set.");
    }

    @Test
    void testSetAndGetProductID() {
        // Arrange
        Product product = new Product();
        Integer expectedProductID = 456;

        // Act
        product.setProductID(expectedProductID);
        Integer actualProductID = product.getProductID();

        // Assert
        assertEquals(expectedProductID, actualProductID, "The product ID should match the value set.");
    }

    @Test
    void testSetUniqueID_Null() {
        // Arrange
        Product product = new Product();

        // Act
        product.setUniqueID(null);
        Integer actualUniqueID = product.getUniqueID();

        // Assert
        assertNull(actualUniqueID, "The unique ID should be null after setting a null value.");
    }

    @Test
    void testSetProductID_Null() {
        // Arrange
        Product product = new Product();

        // Act
        product.setProductID(null);
        Integer actualProductID = product.getProductID();

        // Assert
        assertNull(actualProductID, "The product ID should be null after setting a null value.");
    }

    @Test
    void testDefaultValues() {
        // Arrange
        Product product = new Product();

        // Act
        Integer defaultUniqueID = product.getUniqueID();
        Integer defaultProductID = product.getProductID();

        // Assert
        assertNull(defaultUniqueID, "The default value of uniqueID should be null.");
        assertNull(defaultProductID, "The default value of productID should be null.");
    }
}
