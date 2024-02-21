/** 
 * Copyright IBM Corp. 2024
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language governing permissions
 * and limitations under the License.
 */
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
