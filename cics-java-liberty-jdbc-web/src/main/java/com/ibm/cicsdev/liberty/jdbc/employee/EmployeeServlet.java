package com.ibm.cicsdev.liberty.jdbc.employee;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * A HTTP servlet acting as a REST endpoint, registered on the path
 * <code>/employees</code>.
 * <p>
 * Sending a HTTP GET request returns a list of employees in the EMP Db2 table,
 * using JDBC to query the table.
 * <p>
 * No other HTTP methods are supported.
 */
@WebServlet("/employees")
public class EmployeeServlet extends HttpServlet
{

    /** The data source, as defined in server.xml */
    @Resource(lookup = "jdbc/defaultCICSDataSource")
    private DataSource dataSource;

    private EmployeeService employeeService;

    @Override
    public void init() throws ServletException
    {
        employeeService = new EmployeeService(dataSource);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try
        {
            // Get all the employees
            List<Employee> employees = employeeService.getEmployees();
            
            // Write the content
            PrintWriter writer = response.getWriter();
            response.setContentType("text/plain");
            writeTextPlain(employees, writer);
        }
        catch (SQLException e)
        {
            // Print the stack trace for easy debug
            e.printStackTrace();

            // Send a HTTP 500 - Internal Server Error
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to look up employees.");
        }

    }

    private void writeTextPlain(List<Employee> employees, PrintWriter writer)
    {
        for (Employee employee : employees)
        {
            // Write the employee data to the HTTP response.
            writer.println(employee.toString());
        }
    }
}
