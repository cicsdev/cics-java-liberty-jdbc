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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class DoJDBC {
	
	private DataSource dataSource;

	public DoJDBC() throws Exception {
		
		// Use JNDI to find the dataSource
		Context initialContext = new InitialContext();
		dataSource = (DataSource) initialContext.lookup("jdbc/defaultCICSDataSource");		
	}
	
	public String getCurrentTimestamp() throws Exception {
		
		String currentTimeStamp = null;

		// Obtain a DataSource Connection
		Connection connection = dataSource.getConnection();

		// Execute the SQL
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT CURRENT TIMESTAMP FROM SYSIBM.SYSDUMMY1");
		ResultSet resultSet = preparedStatement.executeQuery();
		
		// Get the results
		if (resultSet == null) {
			throw new Exception("Error: SQL query did not return any results");
		}
		resultSet.next();
		currentTimeStamp = resultSet.getTimestamp(1).toString().trim();

		// Commit
		connection.commit();

		// Close the connection
		connection.close();
		
		// Return the user
		return currentTimeStamp;
	}
}
