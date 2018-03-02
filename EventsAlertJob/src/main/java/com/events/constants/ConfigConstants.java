package com.events.constants;

/**
 * @author
 */
public interface ConfigConstants {

	// HSQLDB Constants
	final String DATABASE = "database";
	final String DBUSER = "dbuser";
	final String JDBC_DRIVER = "org.hsqldb.jdbc.JDBCDriver";
	final String HSQL_CONNECTION = "jdbc:hsqldb:hsql://localhost/";
	final String PROP_CASSANDRA_KEYSPACE = "cassandra.keyspace";

	final String CONFIG_PROPERTIES = "config.properties";

	final String TABLE_NAME = "LOG_EVENT";
	final String FILE_PATH = "C:\\Users\\ritipate\\Documents\\Cisco\\EventsAlertJob\\Events.txt";

	final int BATCH_SIZE = 500;

}
