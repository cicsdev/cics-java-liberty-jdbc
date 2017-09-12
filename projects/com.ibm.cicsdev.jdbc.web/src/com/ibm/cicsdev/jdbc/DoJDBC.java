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
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * Implementation class for CICS JDBC sample
 *
 */
public class DoJDBC {
	
	/** Datasource obtained from JNDI lookup */
	private DataSource dataSource;

	/**
	 * Default constructor to lookup datasource in JNDI
	 * 
	 * @throws Exception
	 */
	public DoJDBC() throws Exception {
		
		Context initialContext = new InitialContext();
		dataSource = (DataSource) initialContext.lookup("jdbc/defaultCICSDataSource");		
	}
	
	/**
	 * Use JDBC to get current time from DB2 system table
	 * 
	 * @return String - - formatted time stamp from DB2
	 * @throws SQLException
	 */
	public String getCurrentTimestamp() throws SQLException  {
		
		
		String currentTimeStamp = null;

		// Obtain a DataSource Connection
		Connection connection = dataSource.getConnection();

		// Execute the SQL
		PreparedStatement preparedStatement = connection.prepareStatement("SELECT CURRENT TIMESTAMP FROM SYSIBM.SYSDUMMY1");
		ResultSet resultSet = preparedStatement.executeQuery();
		
		// Get the results
		if (resultSet == null) {
			throw new RuntimeException("Error: SQL query did not return any results");
		}
		resultSet.next();
		currentTimeStamp = resultSet.getTimestamp(1).toString().trim();

		// Commit the connection
		connection.commit();

		// Close the connection
		connection.close();
		
		// Return the user
		return currentTimeStamp;
	}
}
