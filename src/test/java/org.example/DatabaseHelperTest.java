package org.example;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.sql.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DatabaseHelperTest {

    private Connection mockConnection;
    private PreparedStatement mockPreparedStatement;
    private ResultSet mockResultSet;
    private DatabaseHelper databaseHelper;

    @BeforeEach
    void setUp() {
        mockConnection = mock(Connection.class);
        mockPreparedStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
        databaseHelper = new DatabaseHelper(mockConnection);
    }

    @Test
    void testExecuteQuery_Success() throws SQLException {
        // Arrange
        String sql = "SELECT * FROM Customers WHERE age = ?";
        List<Object> parameters = Collections.singletonList(25);

        when(mockConnection.prepareStatement(sql)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);

        // Act
        ResultSet resultSet = databaseHelper.executeQuery(sql, parameters);

        // Assert
        verify(mockConnection).prepareStatement(sql);
        verify(mockPreparedStatement).setObject(1, 25);
        verify(mockPreparedStatement).executeQuery();
        assertNotNull(resultSet, "ResultSet should not be null.");
    }

    @Test
    void testExecuteQuery_SQLException() throws SQLException {
        // Arrange
        String sql = "SELECT * FROM Customers WHERE age = ?";
        List<Object> parameters = Collections.singletonList(25);

        when(mockConnection.prepareStatement(sql)).thenThrow(new SQLException("Mock SQL Exception"));

        // Act & Assert
        assertThrows(SQLException.class, () -> databaseHelper.executeQuery(sql, parameters));
        verify(mockConnection).prepareStatement(sql);
    }

    @Test
    void testExecuteUpdate_Success() throws SQLException {
        // Arrange
        String sql = "UPDATE Customers SET age = ? WHERE id = ?";
        List<Object> parameters = Arrays.asList(30, 1);

        when(mockConnection.prepareStatement(sql)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1); // Assume 1 row affected

        // Act
        Integer rowsAffected = databaseHelper.executeUpdate(sql, parameters);

        // Assert
        verify(mockConnection).prepareStatement(sql);
        verify(mockPreparedStatement).setObject(1, 30);
        verify(mockPreparedStatement).setObject(2, 1);
        verify(mockPreparedStatement).executeUpdate();
        assertEquals(1, rowsAffected, "Expected 1 row to be affected.");
    }

    @Test
    void testExecuteUpdate_NoRowsAffected() throws SQLException {
        // Arrange
        String sql = "UPDATE Customers SET age = ? WHERE id = ?";
        List<Object> parameters = Arrays.asList(30, 1);

        when(mockConnection.prepareStatement(sql)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(0); // Assume no rows affected

        // Act
        Integer rowsAffected = databaseHelper.executeUpdate(sql, parameters);

        // Assert
        assertEquals(0, rowsAffected, "Expected no rows to be affected.");
    }

    @Test
    void testExecuteUpdate_SQLException() throws SQLException {
        // Arrange
        String sql = "INSERT INTO Customers (name, age) VALUES (?, ?)";
        List<Object> parameters = Arrays.asList("John Doe", 25);

        when(mockConnection.prepareStatement(sql)).thenThrow(new SQLException("Mock SQL Exception"));

        // Act & Assert
        assertThrows(SQLException.class, () -> databaseHelper.executeUpdate(sql, parameters));
        verify(mockConnection).prepareStatement(sql);
    }

    @Test
    void testSetParameters_Success() throws SQLException {
        // Arrange
        String sql = "INSERT INTO Customers (name, age, city) VALUES (?, ?, ?)";
        List<Object> parameters = Arrays.asList("John Doe", 25, "New York");

        when(mockConnection.prepareStatement(sql)).thenReturn(mockPreparedStatement);

        // Act
        databaseHelper.executeUpdate(sql, parameters);

        // Assert
        verify(mockPreparedStatement).setObject(1, "John Doe");
        verify(mockPreparedStatement).setObject(2, 25);
        verify(mockPreparedStatement).setObject(3, "New York");
    }

    @Test
    void testClose_Success() throws SQLException {
        // Arrange
        when(mockConnection.isClosed()).thenReturn(false);

        // Act
        databaseHelper.close();

        // Assert
        verify(mockConnection).close();
    }

    @Test
    void testClose_AlreadyClosed() throws SQLException {
        // Arrange
        when(mockConnection.isClosed()).thenReturn(true);

        // Act
        databaseHelper.close();

        // Assert
        verify(mockConnection, times(0)).close(); // Ensure close() is not called again
    }

    @Test
    void testExecuteUpdate_PrepareStatementFailure() throws SQLException {
        // Arrange
        String sql = "DELETE FROM Customers WHERE id = ?";
        List<Object> parameters = Collections.singletonList(1);

        when(mockConnection.prepareStatement(sql)).thenThrow(new SQLException("Mock PrepareStatement Exception"));

        // Act & Assert
        SQLException exception = assertThrows(SQLException.class, () -> databaseHelper.executeUpdate(sql, parameters));
        assertEquals("Mock PrepareStatement Exception", exception.getMessage());
    }

    @Test
    void testExecuteQuery_InvalidResultSetAccess() throws SQLException {
        // Arrange
        String sql = "SELECT * FROM Customers WHERE age = ?";
        List<Object> parameters = Collections.singletonList(25);

        when(mockConnection.prepareStatement(sql)).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.getString(anyInt())).thenThrow(new SQLException("Invalid column access"));

        // Act
        ResultSet resultSet = databaseHelper.executeQuery(sql, parameters);

        // Assert: Accessing the ResultSet causes an exception
        assertThrows(SQLException.class, () -> resultSet.getString(1));
    }
}





