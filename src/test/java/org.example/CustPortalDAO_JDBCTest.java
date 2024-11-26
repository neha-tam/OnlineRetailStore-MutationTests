package org.example;

import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class CustPortalDAO_JDBCTest {

    static Connection dbConnection;
    private static DatabaseHelper dbHelper;
    private DatabaseHelper mockDbHelper; // Mocked DatabaseHelper
    private static CustPortalDAO_JDBC custPortalDAO;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeAll
    public static void setup() throws SQLException {
        // Initialize the database connection
        dbConnection = DriverManager.getConnection(
                "jdbc:mysql://localhost/retailStore", "<username>", "<password>");
        dbConnection.setAutoCommit(false); // Enable transactions for test isolation
        dbHelper = new DatabaseHelper(dbConnection);
        custPortalDAO = new CustPortalDAO_JDBC(dbConnection, dbHelper);
    }

    @BeforeEach
    public void setUpStreams() {

        System.setProperty("isTest", "true");
        // Mock the DatabaseHelper
        mockDbHelper = mock(DatabaseHelper.class);

        // Set up a stream to capture console output
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out; // Save the original System.out
        System.setOut(new PrintStream(outputStream)); // Redirect System.out to the stream
    }

    @AfterEach
    public void tearDownStreams() throws SQLException {
        System.setOut(originalOut); // Restore the original System.out
        dbConnection.rollback(); // Rollback changes after each test
    }

    @AfterAll
    public static void teardown() throws SQLException {
        dbConnection.close(); // Close the database connection
    }

    @Test
    public void testOrderItemsByPrice_NoItemsInInventory() throws SQLException {
        // Clear inventory for this test
        dbHelper.executeUpdate("DELETE FROM Product", new ArrayList<>());
        dbHelper.executeUpdate("DELETE FROM Inventory", new ArrayList<>());

        // Simulate pressing "Enter"
        ByteArrayInputStream inputStream = new ByteArrayInputStream("\n".getBytes());
        System.setIn(inputStream);

        System.setProperty("isTest", "true");

        // Call the method
        custPortalDAO.OrderItemsByPrice();

        // Capture and validate the console output
        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("No items currently available.Sorry!"),
                "Expected message 'No items currently available.Sorry!' was not found in the console output.");
    }

    @Test
    public void testOrderItemsByPrice_NotEmpty() throws SQLException {
        // Clear inventory for this test
        dbHelper.executeUpdate("DELETE FROM Product", new ArrayList<>());
        dbHelper.executeUpdate("DELETE FROM Inventory", new ArrayList<>());


        String insert_inv = "insert into Inventory values (?,?,?,?);";
        List<Object> inv_args_1 = new ArrayList<>();
        inv_args_1.add(1);
        inv_args_1.add("Piano");
        inv_args_1.add(40000);
        inv_args_1.add(4);
        int res = dbHelper.executeUpdate(insert_inv, inv_args_1);

        List<Object> inv_args_2 = new ArrayList<>();
        inv_args_2.add(2);
        inv_args_2.add("Tabla");
        inv_args_2.add(10000);
        inv_args_2.add(5);
        res = dbHelper.executeUpdate(insert_inv, inv_args_2);

        String insert_prod = "insert into Product values (?,?);";
        List<Object> prod_args_1 = new ArrayList<>();
        prod_args_1.add(100);
        prod_args_1.add(1);
        res = dbHelper.executeUpdate(insert_prod, prod_args_1);

        List<Object> prod_args_2 = new ArrayList<>();
        prod_args_2.add(101);
        prod_args_2.add(2);
        res = dbHelper.executeUpdate(insert_prod, prod_args_2);


        // Simulate pressing "Enter"
        ByteArrayInputStream inputStream = new ByteArrayInputStream("\n".getBytes());
        System.setIn(inputStream);

        System.setProperty("isTest", "true");

        // Call the method
        custPortalDAO.OrderItemsByPrice();

        // Capture and validate the console output
        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("ProductID" + "\t" + "Product" + "\t" + "Price" + "\t" + "Quantity"),
                "Expected line 'ProductID\tProduct\tPrice\tQuantity' was not found in the console output.");
        assertTrue(consoleOutput.contains("1\t\tPiano\t40000\t4"), "Expected line '1\t\tPiano\t40000\t4' was not found in the console output.");
        assertTrue(consoleOutput.contains("2\t\tTabla\t10000\t5"), "Expected line '2\t\tTabla\t10000\t5' was not found in the console output.");

    }


    @Test
    public void testCheckInventory_NoItems() throws SQLException {
        // Clear inventory for this test
        dbHelper.executeUpdate("DELETE FROM Product", new ArrayList<>());
        dbHelper.executeUpdate("DELETE FROM Inventory", new ArrayList<>());

        // Simulate pressing "Enter"
        ByteArrayInputStream inputStream = new ByteArrayInputStream("\n".getBytes());
        System.setIn(inputStream);

        System.setProperty("isTest", "true");

        // Call the method
        custPortalDAO.checkInventory();

        // Capture and validate the console output
        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("No items currently available.Sorry!"),
                "Expected message 'No items currently available.Sorry!' was not found in the console output.");
    }

    @Test
    public void testCheckInventory_NotEmpty() throws SQLException {
        // Clear inventory for this test
        dbHelper.executeUpdate("DELETE FROM Product", new ArrayList<>());
        dbHelper.executeUpdate("DELETE FROM Inventory", new ArrayList<>());


        String insert_inv = "insert into Inventory values (?,?,?,?);";
        List<Object> inv_args_1 = new ArrayList<>();
        inv_args_1.add(1);
        inv_args_1.add("Piano");
        inv_args_1.add(40000);
        inv_args_1.add(4);
        int res = dbHelper.executeUpdate(insert_inv, inv_args_1);

        List<Object> inv_args_2 = new ArrayList<>();
        inv_args_2.add(2);
        inv_args_2.add("Tabla");
        inv_args_2.add(10000);
        inv_args_2.add(5);
        res = dbHelper.executeUpdate(insert_inv, inv_args_2);

        String insert_prod = "insert into Product values (?,?);";
        List<Object> prod_args_1 = new ArrayList<>();
        prod_args_1.add(100);
        prod_args_1.add(1);
        res = dbHelper.executeUpdate(insert_prod, prod_args_1);

        List<Object> prod_args_2 = new ArrayList<>();
        prod_args_2.add(101);
        prod_args_2.add(2);
        res = dbHelper.executeUpdate(insert_prod, prod_args_2);


        // Simulate pressing "Enter"
        ByteArrayInputStream inputStream = new ByteArrayInputStream("\n".getBytes());
        System.setIn(inputStream);

        System.setProperty("isTest", "true");

        // Call the method
        custPortalDAO.checkInventory();

        // Capture and validate the console output
        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("ProductID\tProduct\tPrice\tQuantity"),
                "Expected line 'ProductID\tProduct\tPrice\tQuantity' was not found in the console output.");
        assertTrue(consoleOutput.contains("1\t\tPiano\t40000\t4"), "Expected line '1\t\tPiano\t40000\t4' was not found in the console output.");
        assertTrue(consoleOutput.contains("2\t\tTabla\t10000\t5"), "Expected line '2\t\tTabla\t10000\t5' was not found in the console output.");

    }

    @Test
    public void testBuyItems_InsufficientStock() throws SQLException {
        // Insert an item with zero stock
        dbHelper.executeUpdate("DELETE FROM Product", new ArrayList<>());
        dbHelper.executeUpdate("DELETE FROM Inventory", new ArrayList<>());
        dbHelper.executeUpdate("INSERT INTO Inventory (productID, productName, price, quantity) VALUES (1, 'Product1', 10, 0)", new ArrayList<>());

        // Attempt to buy more than available
        Customer customer = new Customer(1, "John Doe");
        Integer transactionID = custPortalDAO.generateTransactionID();
        Integer transNo = custPortalDAO.getNextTransNo();

        custPortalDAO.buyItems(transNo, customer, transactionID, 1, 5);

        // Capture and validate the console output
        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("Please check the inventory below and buy a valid number of products."),
                "Expected message 'Please check the inventory below and buy a valid number of products.' was not found in the console output.");

        dbHelper.executeUpdate("DELETE FROM Product", new ArrayList<>());
        dbHelper.executeUpdate("DELETE FROM Inventory", new ArrayList<>());
        dbHelper.executeUpdate("INSERT INTO Inventory (productID, productName, price, quantity) VALUES (1, 'Product1', 10, 5)", new ArrayList<>());

        // Attempt to buy more than available
        transactionID = custPortalDAO.generateTransactionID();
        transNo = custPortalDAO.getNextTransNo();

        custPortalDAO.buyItems(transNo, customer, transactionID, 1, 0);

        // Capture and validate the console output
        assertTrue(consoleOutput.contains("Please check the inventory below and buy a valid number of products."),
                "Expected message 'Please check the inventory below and buy a valid number of products.' was not found in the console output.");
    }

    @Test
    public void testBuyItems_NoStock() throws SQLException {
        // Insert an item with zero stock
        dbHelper.executeUpdate("DELETE FROM Product", new ArrayList<>());
        dbHelper.executeUpdate("DELETE FROM Inventory", new ArrayList<>());
        // Attempt to buy more than available
        Customer customer = new Customer(1, "John Doe");
        Integer transactionID = custPortalDAO.generateTransactionID();
        Integer transNo = custPortalDAO.getNextTransNo();

        custPortalDAO.buyItems(transNo, customer, transactionID, 1, 5);

        // Capture and validate the console output
        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("Item not in stock. Please try again later!"),
                "Expected message 'Item not in stock. Please try again later!' was not found in the console output.");
    }

    @Test
    public void testBuyItems_SQLException() throws SQLException {
        CustPortalDAO_JDBC mockCustPortalDAO = new CustPortalDAO_JDBC(dbConnection, mockDbHelper);

        when(mockDbHelper.executeQuery(anyString(), anyList())).thenThrow(new SQLException("Inventory SQL Exception", "08002", 888));

        // Redirect System.in to simulate pressing "Enter"
        ByteArrayInputStream inputStream = new ByteArrayInputStream("\n".getBytes());
        System.setIn(inputStream);

        // Act
        Customer customer = new Customer(1, "John Doe");
        Integer transactionID = custPortalDAO.generateTransactionID();
        Integer transNo = custPortalDAO.getNextTransNo();

        mockCustPortalDAO.buyItems(transNo, customer, transactionID, 1, 5);

        // Assert: Verify console output for SQLException handling
        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("SQLException: Inventory SQL Exception"),
                "Expected 'SQLException' message not found in the console output.");
        assertTrue(consoleOutput.contains("SQLState: 08002"),
                "Expected 'SQLState' message not found in the console output.");
        assertTrue(consoleOutput.contains("VendorError: 888"),
                "Expected 'VendorError' message not found in the console output.");


    }

    @Test
    public void testGenerateReceipt_NotEmpty() throws SQLException {
        // Clear inventory for this test
        dbHelper.executeUpdate("DELETE FROM Product", new ArrayList<>());
        dbHelper.executeUpdate("DELETE FROM Inventory", new ArrayList<>());
        dbHelper.executeUpdate("DELETE FROM Transaction", new ArrayList<>());


        String insert_inv = "insert into Inventory values (?,?,?,?);";
        List<Object> inv_args_1 = new ArrayList<>();
        inv_args_1.add(1);
        inv_args_1.add("Piano");
        inv_args_1.add(40000);
        inv_args_1.add(4);
        int res = dbHelper.executeUpdate(insert_inv, inv_args_1);

        List<Object> inv_args_2 = new ArrayList<>();
        inv_args_2.add(2);
        inv_args_2.add("Tabla");
        inv_args_2.add(10000);
        inv_args_2.add(5);
        res = dbHelper.executeUpdate(insert_inv, inv_args_2);

        String insert_prod = "insert into Product values (?,?);";
        List<Object> prod_args_1 = new ArrayList<>();
        prod_args_1.add(100);
        prod_args_1.add(1);
        res = dbHelper.executeUpdate(insert_prod, prod_args_1);

        List<Object> prod_args_2 = new ArrayList<>();
        prod_args_2.add(101);
        prod_args_2.add(2);
        res = dbHelper.executeUpdate(insert_prod, prod_args_2);


        // Simulate pressing "Enter"
        ByteArrayInputStream inputStream = new ByteArrayInputStream("\n".getBytes());
        System.setIn(inputStream);

        System.setProperty("isTest", "true");


        // Call the method
        Customer cust = new Customer(1, "Ketaki");
        custPortalDAO.buyItems(1, cust, 100, 1, 2);
        custPortalDAO.buyItems(2, cust, 100, 2, 3);

        custPortalDAO.generateReceipt(cust, 100);

        // Capture and validate the console output
        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("Receipt:"),
                "Expected line 'Receipt:' was not found in the console output.");
        assertTrue(consoleOutput.contains("Transaction ID: 100"),
                "Expected line 'Transaction ID: 100' was not found in the console output.");
        assertTrue(consoleOutput.contains("Customer ID: 1"),
                "Expected line 'Customer ID: 1' was not found in the console output.");
        assertTrue(consoleOutput.contains("CustomerName: Ketaki"),
                "Expected line 'CustomerName: Ketaki' was not found in the console output.");

        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String strDate = currentDate.format(formatter);
        assertTrue(consoleOutput.contains("Date:" + strDate),
                "Expected date 'Date:' was not found in the console output.");

        assertTrue(consoleOutput.contains("Product\t\tQuantity\tPrice"), "Expected line 'Product\t\tQuantity\tPrice' was not found in the console output.");
        assertTrue(consoleOutput.contains("Piano\t\t2\t\t80000"), "Expected line 'Piano\t\t2\t\t80000' was not found in the console output.");
        assertTrue(consoleOutput.contains("Tabla\t\t3\t\t30000"), "Expected line 'Tabla\t\t3\t\t30000' was not found in the console output.");
        assertTrue(consoleOutput.contains("Total Cost = 110000/-"), "Expected line 'Total Cost = 110000/-' was not found in the console output.");
        assertTrue(consoleOutput.contains("Thanks for shopping with us!"), "Expected line 'Thanks for shopping with us!' was not found in the console output.");

    }

    @Test
    public void testGenerateReceipt_NoTransactions() throws SQLException {
        // Clear transactions for this test
        dbHelper.executeUpdate("DELETE FROM Transaction", new ArrayList<>());

        // Attempt to generate receipt for a nonexistent transaction
        Customer customer = new Customer(1, "John Doe");
        custPortalDAO.generateReceipt(customer, 999);

        // Capture and validate the console output
        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("No transactions to show."),
                "Expected message 'No transactions to show.' was not found in the console output.");
    }

    @Test
    public void testGenerateReceipt_WithTransactions() throws SQLException {
        // Insert inventory and a transaction
        dbHelper.executeUpdate("DELETE FROM Product", new ArrayList<>());
        dbHelper.executeUpdate("DELETE FROM Inventory", new ArrayList<>());
        dbHelper.executeUpdate("DELETE FROM Transaction", new ArrayList<>());
        dbHelper.executeUpdate("INSERT INTO Inventory (productID, productName, price, quantity) VALUES (1, 'Product1', 10, 5)", new ArrayList<>());
        dbHelper.executeUpdate("INSERT INTO Transaction (transactionID, transactionNo, customerID, dateOfPurchase, productID, quantityBought) VALUES (100, 1, 1, '2024-11-21', 1, 2)", new ArrayList<>());

        // Generate receipt
        Customer customer = new Customer(1, "John Doe");
        custPortalDAO.generateReceipt(customer, 100);

        // Capture and validate the console output
        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("Receipt:"),
                "Expected message 'Receipt:' was not found in the console output.");
        assertTrue(consoleOutput.contains("Product1"),
                "Expected product 'Product1' was not found in the console output.");
        assertTrue(consoleOutput.contains("2024-11-21"),
                "Expected date '2024-11-21' was not found in the console output.");
    }

    @Test
    void testGenerateReceipt_SQLException() throws SQLException {
        CustPortalDAO_JDBC mockCustPortalDAO = new CustPortalDAO_JDBC(dbConnection, mockDbHelper);

        // Arrange: Simulate SQLException in dbHelper
        when(mockDbHelper.executeQuery(anyString(), anyList())).thenThrow(new SQLException("Inventory SQL Exception", "08002", 888));

        // Redirect System.in to simulate pressing "Enter"
        ByteArrayInputStream inputStream = new ByteArrayInputStream("\n".getBytes());
        System.setIn(inputStream);

        // Act
        Customer customer = new Customer(1, "John Doe");
        mockCustPortalDAO.generateReceipt(customer, 100);

        // Assert: Verify console output for SQLException handling
        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("SQLException: Inventory SQL Exception"),
                "Expected 'SQLException' message not found in the console output.");
        assertTrue(consoleOutput.contains("SQLState: 08002"),
                "Expected 'SQLState' message not found in the console output.");
        assertTrue(consoleOutput.contains("VendorError: 888"),
                "Expected 'VendorError' message not found in the console output.");
    }


    @Test
    void testOrderItemsByPrice_SQLException() throws SQLException {

        CustPortalDAO_JDBC mockCustPortalDAO = new CustPortalDAO_JDBC(dbConnection, mockDbHelper);

        // Arrange: Simulate SQLException in dbHelper
        when(mockDbHelper.executeQuery(anyString(), anyList())).thenThrow(new SQLException("Test SQL Exception", "08001", 999));

        // Redirect System.in to simulate pressing "Enter"
        ByteArrayInputStream inputStream = new ByteArrayInputStream("\n".getBytes());
        System.setIn(inputStream);

        // Act
        mockCustPortalDAO.OrderItemsByPrice(); // Use the real DAO object with mocked dbHelper

        // Assert: Verify console output for SQLException handling
        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("SQLException: Test SQL Exception"),
                "Expected 'SQLException' message not found in the console output.");
        assertTrue(consoleOutput.contains("SQLState: 08001"),
                "Expected 'SQLState' message not found in the console output.");
        assertTrue(consoleOutput.contains("VendorError: 999"),
                "Expected 'VendorError' message not found in the console output.");
    }


    @Test
    void testCheckInventory_SQLException() throws SQLException {
        CustPortalDAO_JDBC mockCustPortalDAO = new CustPortalDAO_JDBC(dbConnection, mockDbHelper);

        // Arrange: Simulate SQLException in dbHelper
        when(mockDbHelper.executeQuery(anyString(), anyList())).thenThrow(new SQLException("Inventory SQL Exception", "08002", 888));

        // Redirect System.in to simulate pressing "Enter"
        ByteArrayInputStream inputStream = new ByteArrayInputStream("\n".getBytes());
        System.setIn(inputStream);

        // Act
        mockCustPortalDAO.checkInventory();

        // Assert: Verify console output for SQLException handling
        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("SQLException: Inventory SQL Exception"),
                "Expected 'SQLException' message not found in the console output.");
        assertTrue(consoleOutput.contains("SQLState: 08002"),
                "Expected 'SQLState' message not found in the console output.");
        assertTrue(consoleOutput.contains("VendorError: 888"),
                "Expected 'VendorError' message not found in the console output.");
    }

    @Test
    void testGenerateTransactionId() throws SQLException {
        int transID = 0;
        dbHelper.executeUpdate("DELETE FROM Transaction;", new ArrayList<>());
        String checkTableSize = "select count(*) as countTrans from Transaction";
        String addTrans = "insert into Transaction (transactionNo,transactionID) VALUES (?,?)";
        List<Object> args = new ArrayList<>();
        args.add(1);
        args.add(101);
        dbHelper.executeUpdate(addTrans, args);

        ResultSet rs_size = dbHelper.executeQuery(checkTableSize, new ArrayList<>());
        if (rs_size.next() && rs_size.getInt("countTrans") == 0) {
            transID = 100;
        } else {
            String checkSql = "select max(transactionID) as max_id from Transaction";
            ResultSet rs = dbHelper.executeQuery(checkSql, new ArrayList<>());

            if (rs.next()) {
                transID = rs.getInt("max_id") + 1;
            }

        }

        int testTransID = custPortalDAO.generateTransactionID();

        assertEquals(transID, testTransID, "getTransactionID() does not return the correct transaction ID.");


        dbHelper.executeUpdate("DELETE from Transaction", new ArrayList<>());
        testTransID = custPortalDAO.generateTransactionID();
        assertEquals(100, testTransID, "getTransactionID() does not return the correct transaction ID.");


    }

    @Test
    void testGenerateTransactionId_SQLException() throws SQLException {
        CustPortalDAO_JDBC mockCustPortalDAO = new CustPortalDAO_JDBC(dbConnection, mockDbHelper);

        // Arrange: Simulate SQLException in dbHelper
        when(mockDbHelper.executeQuery(anyString(), anyList())).thenThrow(new SQLException("Inventory SQL Exception", "08002", 888));

        // Redirect System.in to simulate pressing "Enter"
        ByteArrayInputStream inputStream = new ByteArrayInputStream("\n".getBytes());
        System.setIn(inputStream);

        // Act
        mockCustPortalDAO.generateTransactionID();

        // Assert: Verify console output for SQLException handling
        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("SQLException: Inventory SQL Exception"),
                "Expected 'SQLException' message not found in the console output.");
        assertTrue(consoleOutput.contains("SQLState: 08002"),
                "Expected 'SQLState' message not found in the console output.");
        assertTrue(consoleOutput.contains("VendorError: 888"),
                "Expected 'VendorError' message not found in the console output.");
    }


    @Test
    void testGetNewCustomerID_SuccessfulRegistration() throws SQLException {
        // Arrange
        String customerName = "Test Customer";

        // Clear existing customers
        dbHelper.executeUpdate("DELETE FROM Customer", new ArrayList<>());

        // Act
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        Integer generatedId = custPortalDAO.getNewCustomerID(customerName);

        // Assert
        // Verify customer was created
        String verifySql = "SELECT * FROM Customer WHERE customerID = ?";
        List<Object> verifyArgs = new ArrayList<>();
        verifyArgs.add(generatedId);
        ResultSet rs = dbHelper.executeQuery(verifySql, verifyArgs);

        assertTrue(rs.next(), "Customer should be created");
        assertEquals(customerName, rs.getString("customerName"), "Customer name should match");

        // Verify success message
        String output = outputStream.toString();
        assertTrue(output.contains("Successfully registered!"),
                "Should show successful registration message");
    }

    @Test
    void testGetNewCustomerID_RegistrationFailure() throws SQLException {
        // Arrange: Ensure the database table is in a state to test
        dbHelper.executeUpdate("ALTER TABLE Customer MODIFY COLUMN customerID INT NOT NULL", new ArrayList<>());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> custPortalDAO.getNewCustomerID(null), // Pass null to trigger the exception
                "Should throw IllegalArgumentException when name is null or empty"
        );

        // Verify the exception message
        assertEquals("Customer name cannot be null or empty", exception.getMessage(),
                "The exception message should indicate the name is invalid");

        // Clean up: Restore the table state after the test
        dbHelper.executeUpdate("ALTER TABLE Customer MODIFY COLUMN customerID INT", new ArrayList<>());
    }

    @Test
    void testGetNewCustomerID_InsertFailure() throws SQLException {
        // Arrange: Mock `dbHelper.executeUpdate` to return 0 (indicating failure)
        DatabaseHelper mockDbHelper = mock(DatabaseHelper.class);
        Connection mockConnection = mock(Connection.class);

        // Mock executeQuery to simulate a valid customer table state
        ResultSet mockResultSet = mock(ResultSet.class);
        when(mockDbHelper.executeQuery(anyString(), anyList())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1); // Simulate a non-empty table with `MAX(customerID)`

        // Mock executeUpdate to return 0 (failure)
        when(mockDbHelper.executeUpdate(anyString(), anyList())).thenReturn(0);

        CustPortalDAO_JDBC custPortalDAO = new CustPortalDAO_JDBC(mockConnection, mockDbHelper);

        // Capture console output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Act
        try {
            custPortalDAO.getNewCustomerID("FailCustomer");
        } catch (SQLException ex) {
            // Expected exception
        }

        // Assert: Verify console output for the failure message
        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("Couldn't register :("),
                "Expected 'Couldn't register :(' message was not found in the console output.");
    }


    @Test
    void testGetNewCustomerID_RollbackFailure() throws SQLException {
        // Arrange: Set up mocks to simulate a rollback failure
        DatabaseHelper mockDbHelper = mock(DatabaseHelper.class);
        Connection mockConnection = mock(Connection.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        // Simulate `SELECT COUNT(*) FROM Customer` returning 1
        when(mockDbHelper.executeQuery(eq("SELECT COUNT(*) FROM Customer"), anyList()))
                .thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1); // Simulate a non-empty table

        // Simulate `SELECT MAX(customerID) FROM Customer` returning a valid ID
        when(mockDbHelper.executeQuery(eq("SELECT MAX(customerID) FROM Customer"), anyList()))
                .thenReturn(mockResultSet);
        when(mockResultSet.getInt(1)).thenReturn(100);

        // Simulate a failure during `dbHelper.executeUpdate` to trigger rollback
        when(mockDbHelper.executeUpdate(anyString(), anyList()))
                .thenThrow(new SQLException("Insert operation failed"));

        // Simulate a failure during rollback
        doThrow(new SQLException("Rollback failed due to database connection issue"))
                .when(mockConnection).rollback();

        CustPortalDAO_JDBC custPortalDAO = new CustPortalDAO_JDBC(mockConnection, mockDbHelper);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream)); // Capture console output

        // Act: Call the method and expect the rollback exception
        try {
            custPortalDAO.getNewCustomerID("TestCustomer");
            fail("Expected SQLException due to rollback failure was not thrown");
        } catch (SQLException ex) {
            // Expected exception
        }

        // Assert: Verify the specific rollback failure message
        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("Rollback failed: Rollback failed due to database connection issue"),
                "Expected 'Rollback failed' message was not found in the console output.");
    }

    @Test
    void testGetNewCustomerID_EmptyTable() throws SQLException {
        // Arrange: Simulate an empty Customer table
        dbHelper.executeUpdate("DELETE FROM Customer", new ArrayList<>()); // Clear the table

        // Capture console output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Act
        int newCustomerId = custPortalDAO.getNewCustomerID("John Doe");

        // Assert: Ensure custID is set to 1
        assertEquals(1, newCustomerId, "Expected new customer ID to be 1 when table is empty.");
        assertTrue(outputStream.toString().contains("Successfully registered!"),
                "Expected 'Successfully registered!' message not found in console output.");
    }

    @Test
    void testGetNewCustomerID_NonEmptyTable() throws SQLException {
        // Arrange: Insert dummy data into the Customer table
        dbHelper.executeUpdate("DELETE FROM Customer", new ArrayList<>()); // Clear the table
        String insertSql = "INSERT INTO Customer (customerID, customerName) VALUES (?, ?)";
        List<Object> params1 = new ArrayList<>();
        params1.add(1); // Existing customer ID
        params1.add("Alice");
        dbHelper.executeUpdate(insertSql, params1);

        List<Object> params2 = new ArrayList<>();
        params2.add(2); // Existing customer ID
        params2.add("Bob");
        dbHelper.executeUpdate(insertSql, params2);

        // Capture console output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Act
        int newCustomerId = custPortalDAO.getNewCustomerID("Charlie");

        // Assert: Ensure custID is set to MAX(customerID) + 1
        assertEquals(3, newCustomerId, "Expected new customer ID to be 3 when MAX(customerID) is 2.");
        assertTrue(outputStream.toString().contains("Successfully registered!"),
                "Expected 'Successfully registered!' message not found in console output.");
    }


    @Test
    void testGenerateTransactionNo() throws SQLException {
        int transID = -1;
        dbHelper.executeUpdate("DELETE FROM Transaction;", new ArrayList<>());
        String checkTableSize = "select count(*) as countTrans from Transaction";
        String addTrans = "insert into Transaction (transactionNo,transactionID) VALUES (?,?)";
        List<Object> args = new ArrayList<>();
        args.add(1);
        args.add(101);
        dbHelper.executeUpdate(addTrans, args);

        ResultSet rs_size = dbHelper.executeQuery(checkTableSize, new ArrayList<>());
        if (rs_size.next() && rs_size.getInt("countTrans") == 0) {
            transID = 100;
        } else {
            String checkSql = "select max(transactionNo) as max_no from Transaction";
            ResultSet rs = dbHelper.executeQuery(checkSql, new ArrayList<>());

            if (rs.next()) {
                transID = rs.getInt("max_no") + 1;
            }

        }

        int testTransNo = custPortalDAO.getNextTransNo();

        assertEquals(transID, testTransNo, "getTransactionNo() does not return the correct transaction number.");


        dbHelper.executeUpdate("DELETE from Transaction", new ArrayList<>());
        testTransNo = custPortalDAO.getNextTransNo();
        assertEquals(1, testTransNo, "getTransactionNo() does not return the correct transaction number.");


    }


    @Test
    public void testGetNewCustomerId_SQLException() throws SQLException {
        // Arrange: Create a mock CustPortalDAO with a mock dbHelper
        CustPortalDAO_JDBC mockCustPortalDAO = new CustPortalDAO_JDBC(dbConnection, mockDbHelper);

        // Configure the mock to throw SQLException
        when(mockDbHelper.executeQuery(anyString(), anyList()))
                .thenThrow(new SQLException("Inventory SQL Exception", "08002", 888));

        // Act and Assert: Capture the console output and assert the exception
        Exception exception = assertThrows(SQLException.class,
                () -> mockCustPortalDAO.getNewCustomerID("TestName"));

        // Validate exception message
        assertEquals("Inventory SQL Exception", exception.getMessage());

        // Capture the console output
        String consoleOutput = outputStream.toString();

        // Validate the output contains the expected error messages
        assertTrue(consoleOutput.contains("SQLException: Inventory SQL Exception"),
                "Expected 'SQLException' message not found in the console output.");
        assertTrue(consoleOutput.contains("SQLState: 08002"),
                "Expected 'SQLState' message not found in the console output.");
        assertTrue(consoleOutput.contains("VendorError: 888"),
                "Expected 'VendorError' message not found in the console output.");
    }


    @Test
    void testGetNextTransNo_EmptyTable() throws SQLException {
        // Simulate an empty Transaction table by deleting existing records
        dbHelper.executeUpdate("DELETE from Transaction", new ArrayList<>());

        // Act: Call the getNextTransNo method to get the next transaction number
        int nextTransNo = custPortalDAO.getNextTransNo();

        // Assert: Verify that the transaction number is 1 since the table is empty
        assertEquals(1, nextTransNo, "Expected transaction number to be 1 for an empty table.");
    }

    @Test
    void testGetNextTransNo_WithExistingTransactions() throws SQLException {
        // Simulate an empty Transaction table by deleting existing records
        dbHelper.executeUpdate("DELETE from Transaction", new ArrayList<>());

        // Insert a dummy transaction into the Transaction table
        String insertDummyTransaction = "INSERT INTO Transaction (transactionNo, transactionID, customerID, dateOfPurchase, productID, quantityBought) VALUES (1, 1001, 1, '2024-11-20', 1, 2)";
        dbHelper.executeUpdate(insertDummyTransaction, new ArrayList<>());

        // Act: Call the getNextTransNo method to get the next transaction number
        int nextTransNo = custPortalDAO.getNextTransNo();

        // Assert: Verify that the transaction number is 2, as the max transactionNo was 1
        assertEquals(2, nextTransNo, "Expected transaction number to be 2 after inserting a dummy transaction.");
    }

    @Test
    void testGetNextTransNo_SQLException() throws SQLException {
        CustPortalDAO_JDBC mockCustPortalDAO = new CustPortalDAO_JDBC(dbConnection, mockDbHelper);

        // Arrange: Simulate SQLException in dbHelper
        when(mockDbHelper.executeQuery(anyString(), anyList())).thenThrow(new SQLException("Inventory SQL Exception", "08002", 888));

        // Redirect System.in to simulate pressing "Enter"
        ByteArrayInputStream inputStream = new ByteArrayInputStream("\n".getBytes());
        System.setIn(inputStream);

        // Act
        mockCustPortalDAO.getNextTransNo();

        // Assert: Verify console output for SQLException handling
        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("SQLException: Inventory SQL Exception"),
                "Expected 'SQLException' message not found in the console output.");
        assertTrue(consoleOutput.contains("SQLState: 08002"),
                "Expected 'SQLState' message not found in the console output.");
        assertTrue(consoleOutput.contains("VendorError: 888"),
                "Expected 'VendorError' message not found in the console output.");
    }

    @Test
    void testGetCustomer_SQLException() throws SQLException {
        // Arrange: Create a mock database helper and simulate an SQLException
        CustPortalDAO_JDBC mockCustPortalDAO = new CustPortalDAO_JDBC(dbConnection, mockDbHelper);
        when(mockDbHelper.executeQuery(anyString(), anyList()))
                .thenThrow(new SQLException("Customer SQL Exception", "08002", 888));

        // Redirect System.out to capture console output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        // Act: Call the getCustomer method
        Customer customer = mockCustPortalDAO.getCustomer(1);

        // Assert: Verify that a customer object is returned even with an exception
        assertNotNull(customer, "Customer object should not be null.");
        assertNull(customer.getCustomerName(), "Customer name should be null due to the exception.");

        // Assert: Verify console output for SQLException handling
        String consoleOutput = outputStream.toString();
        assertTrue(consoleOutput.contains("SQLException: Customer SQL Exception"),
                "Expected 'SQLException: Customer SQL Exception' not found in console output.");
        assertTrue(consoleOutput.contains("SQLState: 08002"),
                "Expected 'SQLState: 08002' not found in console output.");
        assertTrue(consoleOutput.contains("VendorError: 888"),
                "Expected 'VendorError: 888' not found in console output.");

        // Restore System.out
        System.setOut(originalOut);
    }


    @Test
    void testGetProductName_SQLException() throws SQLException {
        // Arrange: Create mock objects
        CustPortalDAO_JDBC mockCustPortalDAO = new CustPortalDAO_JDBC(dbConnection, mockDbHelper);

        // Simulate SQLException in dbHelper.executeQuery
        when(mockDbHelper.executeQuery(anyString(), anyList()))
                .thenThrow(new SQLException("Inventory SQL Exception", "08002", 888));

        // Redirect System.out to capture console output
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out; // Save the original System.out
        System.setOut(new PrintStream(outputStream)); // Redirect System.out to the stream

        try {
            // Act: Call the method with a mock product ID
            String productName = mockCustPortalDAO.getProductName(1);

            // Assert: Check that the method returned null
            assertNull(productName, "Expected null to be returned when SQLException occurs.");

            // Capture and validate the console output
            String consoleOutput = outputStream.toString();
            assertTrue(consoleOutput.contains("SQLException: Inventory SQL Exception"),
                    "Expected 'SQLException: Inventory SQL Exception' message not found in the console output.");
            assertTrue(consoleOutput.contains("SQLState: 08002"),
                    "Expected 'SQLState: 08002' message not found in the console output.");
            assertTrue(consoleOutput.contains("VendorError: 888"),
                    "Expected 'VendorError: 888' message not found in the console output.");
        } finally {
            // Restore the original System.out
            System.setOut(originalOut);
        }
    }

    @Test
    void testGetCustomer_VerifyCustomerAttributes() throws SQLException {
        // Arrange: Clear existing data and insert a test customer
        Integer testCustId = 1;
        String testCustName = "Test Customer";

        // Delete all existing customers
        dbHelper.executeUpdate("DELETE FROM Customer", new ArrayList<>());

        // Insert a test customer
        String insertSql = "INSERT INTO Customer VALUES (?, ?)";
        List<Object> insertArgs = new ArrayList<>();
        insertArgs.add(testCustId);
        insertArgs.add(testCustName);
        int insertResult = dbHelper.executeUpdate(insertSql, insertArgs);
        assertTrue(insertResult > 0, "Failed to insert test customer");

        // Act: Retrieve the customer
        Customer retrievedCustomer = custPortalDAO.getCustomer(testCustId);

        // Assert: Verify both ID and name were properly set
        // These assertions will fail if the setter methods weren't called
        // or if the while loop condition was mutated to false
        assertNotNull(retrievedCustomer, "Retrieved customer should not be null");
        assertEquals(testCustId, retrievedCustomer.getCustomerID(),
                "Customer ID was not set correctly");
        assertEquals(testCustName, retrievedCustomer.getCustomerName(),
                "Customer name was not set correctly");

        // Additional test to verify the while loop executed
        String verifySql = "SELECT COUNT(*) FROM Customer WHERE customerID = ?";
        List<Object> verifyArgs = new ArrayList<>();
        verifyArgs.add(testCustId);
        ResultSet verifyRs = dbHelper.executeQuery(verifySql, verifyArgs);
        verifyRs.next();
        assertEquals(1, verifyRs.getInt(1),
                "Expected exactly one customer record");
    }

    @Test
    void testGetCustomer_NoMatchingRecord() throws SQLException {
        // Arrange: Clear existing data
        dbHelper.executeUpdate("DELETE FROM Customer", new ArrayList<>());

        // Act: Try to retrieve a non-existent customer
        Integer nonExistentId = 999;
        Customer retrievedCustomer = custPortalDAO.getCustomer(nonExistentId);

        // Assert: Verify the customer object has the ID but null name
        // This verifies that the while loop wasn't executed for non-existent records
        assertNotNull(retrievedCustomer, "Should return a customer object even if not found");
        assertEquals(nonExistentId, retrievedCustomer.getCustomerID(),
                "Customer ID should match input even if not found");
        assertNull(retrievedCustomer.getCustomerName(),
                "Customer name should be null for non-existent customer");
    }


    @Test
    void testBuyItems_InvalidQuantity_High() throws SQLException {
        // Arrange
        System.setProperty("isTest", "true");
        Integer transNo = 1;
        Customer cust = new Customer(1, "Test Customer");
        Integer transID = 1;
        Integer prodID = 1;
        Integer uniqueID = 1;

        // Set up inventory with quantity 5
        dbHelper.executeUpdate("DELETE FROM Product", new ArrayList<>());
        dbHelper.executeUpdate("DELETE FROM Inventory", new ArrayList<>());
        String insertSql = "INSERT INTO Inventory (productID, quantity) VALUES (?, ?)";
        List<Object> insertArgs = new ArrayList<>();
        insertArgs.add(prodID);
        insertArgs.add(5); // Available quantity
        dbHelper.executeUpdate(insertSql, insertArgs);
        String insertProdSql = "INSERT INTO Product (uniqueID,productID) VALUES (?,?)";

        for (int j = 0; j < 5; j++) {
            List<Object> prodArgs = new ArrayList<>();
            uniqueID = j + 1;
            prodArgs.add(uniqueID);
            prodArgs.add(prodID);
            dbHelper.executeUpdate(insertProdSql, prodArgs);
        }


        // Mock user input to avoid NoSuchElementException
//        String simulatedInput = "\n\n"; // Simulate pressing Enter twice
//        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Act & Assert
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        custPortalDAO.buyItems(transNo, cust, transID, prodID, 6); // Trying to buy more than available

        String output = outputStream.toString();
        assertTrue(output.contains("Please check the inventory"),
                "Should show inventory check message for invalid quantity");

        // Verify checkInventory was called by checking if inventory was displayed
        assertTrue(output.contains("Inventory"), "Inventory should be displayed");
    }

    @Test
    void testBuyItems_InvalidQuantity_Low() throws SQLException {
        System.setProperty("isTest", "true");
        // Arrange
        Integer transNo = 1;
        Customer cust = new Customer(1, "Test Customer");
        Integer transID = 1;
        Integer prodID = 1;


        // Set up inventory
        dbHelper.executeUpdate("DELETE FROM Product", new ArrayList<>());
        dbHelper.executeUpdate("DELETE FROM Inventory", new ArrayList<>());
        String insertSql = "INSERT INTO Inventory (productID, quantity) VALUES (?, ?)";
        List<Object> insertArgs = new ArrayList<>();
        insertArgs.add(prodID);
        insertArgs.add(5);
        dbHelper.executeUpdate(insertSql, insertArgs);


        // Act & Assert
        // Testing quantity < 1 condition
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        custPortalDAO.buyItems(transNo, cust, transID, prodID, 0); // Trying to buy zero quantity

        String output = outputStream.toString();
        assertTrue(output.contains("Please check the inventory"),
                "Should show inventory check message for invalid quantity");
    }

    @Test
    void testBuyItems_ValidQuantity_UpdatesInventory() throws SQLException {
        System.setProperty("isTest", "true");
        // Arrange
        Integer transNo = 1;
        Customer cust = new Customer(1, "Test Customer");
        Integer transID = 1;
        Integer prodID = 1;
        Integer initialQuantity = 5;
        Integer purchaseQuantity = 3;

        // Set up inventory and product
        dbHelper.executeUpdate("DELETE FROM Product", new ArrayList<>());
        dbHelper.executeUpdate("DELETE FROM Inventory", new ArrayList<>());
        dbHelper.executeUpdate("DELETE FROM Transaction", new ArrayList<>());

        // Insert inventory
        String insertInvSql = "INSERT INTO Inventory (productID, quantity) VALUES (?, ?)";
        List<Object> invArgs = new ArrayList<>();
        invArgs.add(prodID);
        invArgs.add(initialQuantity);
        dbHelper.executeUpdate(insertInvSql, invArgs);

        // Insert products
        String insertProdSql = "INSERT INTO Product (uniqueID,productID) VALUES (?,?)";
        for (int i = 0; i < initialQuantity; i++) {
            List<Object> prodArgs = new ArrayList<>();
            prodArgs.add(100 + i);
            prodArgs.add(prodID);
            dbHelper.executeUpdate(insertProdSql, prodArgs);
        }

        // Mock user input to avoid NoSuchElementException
        String simulatedInput = "\n\n"; // Simulate pressing Enter twice
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        // Act
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        custPortalDAO.buyItems(transNo, cust, transID, prodID, purchaseQuantity);

        // Assert
        // Verify the loop executed correct number of times by checking remaining inventory
        String checkInvSql = "SELECT quantity FROM Inventory WHERE productID = ?";
        List<Object> checkArgs = new ArrayList<>();
        checkArgs.add(prodID);
        ResultSet rs = dbHelper.executeQuery(checkInvSql, checkArgs);
        rs.next();
        int remainingQuantity = rs.getInt("quantity");
        assertEquals(initialQuantity - purchaseQuantity, remainingQuantity,
                "Inventory should be reduced by purchase quantity");

        // Verify transaction was recorded
        String checkTransSql = "SELECT COUNT(*) FROM Transaction WHERE transactionNo = ?";
        List<Object> transArgs = new ArrayList<>();
        transArgs.add(transNo);
        rs = dbHelper.executeQuery(checkTransSql, transArgs);
        rs.next();
        assertEquals(1, rs.getInt(1), "Transaction should be recorded");

        // Verify products were removed
        String checkProdSql = "SELECT COUNT(*) FROM Product WHERE productID = ?";
        List<Object> prodCheckArgs = new ArrayList<>();
        prodCheckArgs.add(prodID);
        rs = dbHelper.executeQuery(checkProdSql, prodCheckArgs);
        rs.next();
        assertEquals(initialQuantity - purchaseQuantity, rs.getInt(1),
                "Products should be removed from Product table");

        // Verify success message
        String output = outputStream.toString();
        assertTrue(output.contains("Item successfully bought"),
                "Should show success message for valid purchase");
    }

};
