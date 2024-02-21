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
package com.ibm.cicsdev.liberty.jdbc.employee;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

/**
 * Service that provides the ability to look up employees from the EMP database.
 */
public class EmployeeService
{
    /** The query that will be run on the Db2 EMP database */
    private static final String QUERY = "select EMPNO, FIRSTNME, LASTNAME, SALARY from EMP";

    private final DataSource dataSource;

    public EmployeeService(DataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    /**
     * Gets a list of employee from the database.
     * 
     * @return A list of employee.
     * @throws SQLException
     *             If database interaction fails.
     */
    public List<Employee> getEmployees() throws SQLException
    {
        List<Employee> employees = new ArrayList<>();

        try (Connection connection = this.dataSource.getConnection())
        {
            // Prepare the SQL SELECT statement
            PreparedStatement statement = connection.prepareStatement(QUERY);

            // Execute the SELECT query
            ResultSet results = statement.executeQuery();

            // Generate an employee from each result
            while (results.next())
            {
                employees.add(getEmployeeFromResults(results));
            }
        }

        return employees;
    }

    /**
     * Marshalls the data from a ResultSet into an {@link Employee} object.
     * 
     * @param result
     *            The ResultSet to load the data from, with the cursor on the
     *            current row.
     * @return An Employee object with all field populated.
     * @throws SQLException
     *             If the employee columns are not accessible.
     */
    private Employee getEmployeeFromResults(ResultSet results) throws SQLException
    {
        String empno = results.getString("EMPNO");
        String firstName = results.getString("FIRSTNME");
        String lastName = results.getString("LASTNAME");
        BigDecimal salary = results.getBigDecimal("SALARY");

        return new Employee(empno, firstName, lastName, salary);
    }
}
