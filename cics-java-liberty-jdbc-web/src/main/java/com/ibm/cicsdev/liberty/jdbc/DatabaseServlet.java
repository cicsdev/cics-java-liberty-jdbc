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

import java.io.IOException;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

/**
 * Servlet that responds with the current database timestamp.
 */
@WebServlet("/database")
public class DatabaseServlet extends HttpServlet
{
    /** The data source */
    @Resource(lookup = "jdbc/defaultCICSDataSource")
    private DataSource dataSource;

    /** The database service */
    private DatabaseService service;

    @Override
    public void init() throws ServletException
    {
        this.service = new DatabaseService(this.dataSource);
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        try
        {
            // Get the current timestamp from the database.
            String timestamp = service.getCurrentTimestamp();

            // Write the response out
            response.setStatus(200);
            response.setContentType("text/plain");
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
