package org.example;

import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.example.CustPortalDAO_JDBCTest.dbConnection;
import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;


class DAO_FactoryTest {

    private DAO_Factory daoFactory;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    void setUp() {
        daoFactory = new DAO_Factory();

        // Set up a stream to capture console output
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out; // Save the original System.out
        System.setOut(new PrintStream(outputStream)); // Redirect System.out to the stream
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut); // Restore the original System.out
    }

    @Test
    void testActivateConnection_Success() throws Exception {
        // Activate the connection
        daoFactory.activateConnection();

        // Verify the connection is active
        assertNotNull(daoFactory.dbconnection, "Database connection should be established.");
        assertTrue(daoFactory.dbconnection.isValid(2), "Database connection should be valid.");

        // Capture and validate the console output
        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("Connecting to database..."),
                "Expected message 'Connecting to database...' was not found in the console output.");
    }

    @Test
    void testActivateConnection_AlreadyActive() throws Exception {
        daoFactory.activateConnection();

        // Try activating again and expect an exception
        Exception exception = assertThrows(Exception.class, daoFactory::activateConnection);
        assertEquals("Connection already active", exception.getMessage());
    }

    @Test
    void testGetCustPortalDAO_WithoutActivation() {
        // Try to get the DAO without activating the connection
        Exception exception = assertThrows(Exception.class, daoFactory::getCustPortalDAO);
        assertEquals("Connection not activated...", exception.getMessage());
    }

    @Test
    void testGetCustPortalDAO_Success() throws Exception {
        // Activate the connection
        daoFactory.activateConnection();

        // Retrieve the DAO
        CustomerPortalDAO custPortalDAO = daoFactory.getCustPortalDAO();
        assertNotNull(custPortalDAO, "CustomerPortalDAO should be instantiated.");
    }

    @Test
    void testDeactivateConnection_Commit() throws Exception {
        // Activate the connection
        daoFactory.activateConnection();

        // Deactivate with commit
        daoFactory.deactivateConnection(DAO_Factory.TXN_STATUS.COMMIT);

        // Verify the connection is closed
        assertFalse(daoFactory.activeConnection, "Connection should be deactivated.");
        assertNull(daoFactory.dbconnection, "Database connection should be null after deactivation.");
    }

    @Test
    void testDeactivateConnection_Rollback() throws Exception {
        // Activate the connection
        daoFactory.activateConnection();

        // Perform some operations (example: no-op)

        // Deactivate with rollback
        daoFactory.deactivateConnection(DAO_Factory.TXN_STATUS.ROLLBACK);

        // Verify the connection is closed
        assertFalse(daoFactory.activeConnection, "Connection should be deactivated.");
        assertNull(daoFactory.dbconnection, "Database connection should be null after deactivation.");
    }

    @Test
    void testDeactivateConnection_NoActiveConnection() {
        // Deactivate without an active connection
        assertDoesNotThrow(() -> daoFactory.deactivateConnection(DAO_Factory.TXN_STATUS.COMMIT),
                "Deactivating a non-active connection should not throw an exception.");
    }

    @Test
    void testDeactivateConnection_NullConnection() throws Exception {
        // Activate and manually set connection to null
        daoFactory.activateConnection();
        daoFactory.dbconnection = null;

        // Deactivate should handle null connection gracefully
        assertDoesNotThrow(() -> daoFactory.deactivateConnection(DAO_Factory.TXN_STATUS.COMMIT),
                "Deactivating with a null connection should not throw an exception.");
    }

    @Test
    void testDatabaseHelperInitialization() throws Exception {
        // Activate the connection
        daoFactory.activateConnection();

        // Verify that DatabaseHelper is initialized
        assertNotNull(daoFactory.databaseHelper, "DatabaseHelper should be initialized.");
    }

    @Test
    void testMultipleDAOInstantiation() throws Exception {
        // Activate the connection
        daoFactory.activateConnection();

        // Retrieve DAOs multiple times
        CustomerPortalDAO dao1 = daoFactory.getCustPortalDAO();
        CustomerPortalDAO dao2 = daoFactory.getCustPortalDAO();

        // Ensure the same instance is returned
        assertSame(dao1, dao2, "DAO_Factory should return the same instance of the DAO.");
    }





};


