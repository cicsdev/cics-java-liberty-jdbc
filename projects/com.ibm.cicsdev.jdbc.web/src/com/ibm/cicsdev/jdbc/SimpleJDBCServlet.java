package com.ibm.cicsdev.jdbc;

/* Licensed Materials - Property of IBM                               */
/*                                                                    */
/* SAMPLE                                                             */
/*                                                                    */
/* (c) Copyright IBM Corp. 2017 All Rights Reserved                   */
/*                                                                    */
/* US Government Users Restricted Rights - Use, duplication or        */
/* disclosure restricted by GSA ADP Schedule Contract with IBM Corp   */
/*                                                                    */

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class SimpleJDBCServlet
 */
@WebServlet("/SimpleJDBCServlet")
public class SimpleJDBCServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * Reference to DoJDBC class
	 */
	private DoJDBC doJDBC;

	/**
	 * Servlet initialisation method
	 * 
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		try {
			// Instantiate DoJDBC to lookup the DataSources
			doJDBC = new DoJDBC();
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	/**
	 * HTTP GET - to invoke test
	 *
	 * @param request
	 *            - HTTP request
	 * @param response
	 *            - HTTP response
	 * @throws IOException
	 * @throws ServletException
	 *             - if an error occurs.
	 * 
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		// Set HTML content type
		response.setContentType("text/plain");

		// Initialise variables
		PrintWriter out = response.getWriter();
		String currentTimeStamp;

		// Call DoJDBC to get time stamp
		try {
			currentTimeStamp = doJDBC.getCurrentTimestamp();
			out.println("SimpleJDBCServlet: DB2 CurrentTimeStamp = "
					+ currentTimeStamp);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}
}
