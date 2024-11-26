package org.example;

import java.sql.*;
import java.util.List;

public class DatabaseHelper {

    private Connection dbConnection;

    public DatabaseHelper(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    // Method to execute queries with results (e.g., SELECT)
    public ResultSet executeQuery(String sql, List<Object> parameters) throws SQLException {
        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
        setParameters(preparedStatement, parameters);
        return preparedStatement.executeQuery();
    }

    // Method to execute updates (e.g., INSERT, UPDATE, DELETE)
    public Integer executeUpdate(String sql, List<Object> parameters) throws SQLException {
        PreparedStatement preparedStatement = dbConnection.prepareStatement(sql);
        setParameters(preparedStatement, parameters);
        dbConnection.commit();
        return preparedStatement.executeUpdate();
    }

    // Helper method to set parameters in PreparedStatement
    private void setParameters(PreparedStatement preparedStatement, List<Object> parameters) throws SQLException {
        for (int i = 0; i < parameters.size(); i++) {
            preparedStatement.setObject(i + 1, parameters.get(i));
        }
    }

    public void close() throws SQLException {
        if (dbConnection != null && !dbConnection.isClosed()) {
            dbConnection.close();
        }
    }

}

