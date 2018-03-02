package com.events.connection;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.events.constants.ConfigConstants;
import com.events.utils.PropertyLoader;

/**
 * Dabase connection/table creation class
 * 
 * @author ritipate
 *
 */
public class HSQLDatabase implements ConfigConstants {

	private static final Logger logger = LoggerFactory.getLogger(HSQLDatabase.class);

	public static Connection con = null;
	public static Statement stmt = null;

	/**
	 * Create Database connection
	 * 
	 * @return Connection
	 */
	public Connection createHSQLDBCOnnection() {

		try {
			// load the properties
			PropertyLoader propertyLoader = new PropertyLoader();
			propertyLoader.getPropertyLoader();

			String database = propertyLoader.get(DATABASE);
			String user = propertyLoader.get(DBUSER);

			// Registering the HSQLDB JDBC driver
			Class.forName(JDBC_DRIVER);

			// Creating the connection with HSQLDB
			con = DriverManager.getConnection(HSQL_CONNECTION + database, user, "");
			if (con != null) {
				logger.info("HSQLDB Connection created successfully");

			} else {
				logger.info("Problem with creating connection");
			}

		} catch (Exception e) {
			logger.error("Error in creating connection" + e.getMessage());
		}

		return con;
	}

	/**
	 * Method to create table
	 * 
	 * @param con
	 * @param tableName
	 */
	public int createTable(Connection con, String tableName) {
		int result = 0;
		try {

			stmt = con.createStatement();

			DatabaseMetaData dbm = con.getMetaData();
			ResultSet table = dbm.getTables(null, null, tableName, null);

			// check if table exists else create it
			if (table.next()) {
				logger.info("Table Exists");
			} else {
				result = stmt.executeUpdate("CREATE TABLE " + tableName
						+ " (id VARCHAR(50) NOT NULL, event_duartion VARCHAR(50),type VARCHAR(50), host VARCHAR(50), alert VARCHAR(50),PRIMARY KEY (id));");
				logger.info("Table created successfully");

			}

		} catch (Exception e) {
			logger.error("Error in creating table" + e.getMessage());
		}
		return result;
	}

}