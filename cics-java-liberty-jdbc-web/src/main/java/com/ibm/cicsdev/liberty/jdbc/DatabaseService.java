package com.ibm.cicsdev.liberty.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

public class DatabaseService
{
    private final DataSource dataSource;

    public DatabaseService(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    public String getCurrentTimestamp() throws SQLException
    {
        // Obtain database connection
        try (Connection connection = this.dataSource.getConnection())
        {
            // Execute the SQL query
            PreparedStatement statement = connection.prepareStatement("select CURRENT TIMESTAMP from SYSIBM.SYSDUMMY1");
            ResultSet results = statement.executeQuery();

            // Get the results
            if (!results.next())
            {
                return "Unknown";
            }

            return results.getTimestamp(1).toString().trim();
        }
        // Connection is automatically closed
    }
}
