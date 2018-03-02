package com.events.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import com.events.bean.EventsLogger;
import com.events.job.ServerEventsLogger;

public class ServerEventsLoggerTest {
	static List<EventsLogger> el = new ArrayList<EventsLogger>();
	private ServerEventsLogger loggerTest = new ServerEventsLogger();
	BufferedReader reader = null;

	@BeforeClass

	public static void init() throws SQLException, ClassNotFoundException, IOException {

		// Class.forName("org.hsqldb.jdbc.JDBCDriver");

		EventsLogger event = new EventsLogger();
		event.setId("abdgggddd");
		event.setDuration("5ms");
		event.setHost("Additional Server");
		event.setType("type");
		event.setAlert("true");

		el.add(event);

		// initialize database
		// initDatabase();
	}

	@AfterClass

	public static void destroy() throws SQLException, ClassNotFoundException, IOException {
		try (Connection connection = getConnection(); Statement statement = connection.createStatement();) {
			connection.commit();
		}
	}

	/**
	 * 
	 * Database initialization for testing
	 * 
	 */

	private static void initDatabase() throws SQLException {

		try (Connection connection = getConnection(); Statement statement = connection.createStatement();) {
			statement.executeUpdate(
					"CREATE TABLE log_event_test(id VARCHAR(50) NOT NULL, event_duartion VARCHAR(50),type VARCHAR(50), host VARCHAR(50), alert VARCHAR(50),PRIMARY KEY (id));");
			connection.commit();

		}

	}

	@Before
	public void setup() throws IOException {
		reader = new BufferedReader(new InputStreamReader(
				new FileInputStream("C:\\Users\\ritipate\\Documents\\Cisco\\HSQLDB\\Events.json")));
	}

	@After
	public void teardown() throws IOException {
		if (reader != null) {
			reader.close();
		}

		reader = null;
	}

	/**
	 * 
	 * Create a connection
	 * 
	 * @return connection object
	 * @throws SQLException
	 */

	private static Connection getConnection() throws SQLException {
		Connection conn = null;
		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
			conn = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/eventdb", "SA", "");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return conn;

	}

	@Test
	public void testFileReader() throws IOException {
		assertNotNull(reader);
	}

	@Test
	public void batchInsertTest() {
		try {
			Connection conn = getConnection();
			int a = loggerTest.batchInsert(conn, el, "log_event");
			System.out.println(a);
			assertEquals(a, 1);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
