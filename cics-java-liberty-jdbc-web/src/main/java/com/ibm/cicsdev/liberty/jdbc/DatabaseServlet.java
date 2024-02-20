package com.ibm.cicsdev.liberty.jdbc;

import java.io.IOException;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

@WebServlet("/database")
public class DatabaseServlet extends HttpServlet
{

    @Resource(lookup = "jdbc/defaultCICSDataSource")
    private DataSource dataSource;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setContentType("text/plain");

        DatabaseService action = new DatabaseService(dataSource);

        try
        {
            String timestamp = action.getCurrentTimestamp();

            response.setStatus(200);
            response.getWriter().append("Db2 current timestamp: ").append(timestamp).append(".");
        }
        catch (SQLException e)
        {
            // Print strack trace for debugging
            e.printStackTrace();

            // Return a HTTP 500 Internal Server Error
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().println("Failled to communicate with Db2.");
        }
    }
}
