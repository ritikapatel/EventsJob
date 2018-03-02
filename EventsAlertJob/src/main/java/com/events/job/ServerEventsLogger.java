package com.events.job;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.events.bean.Events;
import com.events.bean.EventsLogger;
import com.events.connection.HSQLDatabase;
import com.events.constants.ConfigConstants;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Job to read json, find the alerts and process it.
 * 
 * @author
 *
 */
public class ServerEventsLogger implements ConfigConstants {

	private static final Logger logger = LoggerFactory.getLogger(ServerEventsLogger.class);

	private static Map<String, Events> eventsMap = new HashMap<String, Events>();
	private static List<EventsLogger> eventList = new ArrayList<EventsLogger>();
	private static Connection con = null;

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {

		logger.info("ssafa");
		ServerEventsLogger ps = new ServerEventsLogger();

		String tableName = TABLE_NAME;
		try {

			HSQLDatabase hsqlDB = new HSQLDatabase();
			// create HSQLDB connection
			con = hsqlDB.createHSQLDBCOnnection();

			// create table if table does not exist
			hsqlDB.createTable(con, tableName);

			// process the event file
			ps.processEventsFile(con, tableName);

		} catch (Exception e) {
			logger.error("Error in processing JSON events" + e.getMessage());
		} finally {

			if (con != null && !con.isClosed()) {
				con.close();
			}
		}

	}

	/**
	 * Process all the events, parse the json and find the alerts. Insert the
	 * time difference with other information into database and flag the alerts.
	 * 
	 * @param con
	 * @param tableName
	 */
	public void processEventsFile(Connection con, String tableName) {

		ObjectMapper mapper = new ObjectMapper();
		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(FILE_PATH)));

			reader.lines().parallel().forEach(line -> {

				try {
					Events evt1 = mapper.readValue(line.toString(), Events.class);
					String alert = null;

					if (eventsMap.containsKey(evt1.getId())) {
						Events evt2 = eventsMap.get(evt1.getId());
						long diff = Math.abs(evt1.getTimestamp() - evt2.getTimestamp());
						if (diff > 4) {
							alert = "true";
						}

						EventsLogger eventLogger = new EventsLogger();

						eventLogger.setId(evt1.getId());
						eventLogger.setAlert(alert);
						eventLogger.setDuration(diff + "ms");
						eventLogger.setHost(evt1.getHost());
						eventLogger.setType(evt1.getType());

						// log id and details
						logger.info("Id " + eventLogger.getId() + " duration " + eventLogger.getDuration() + " alert "
								+ eventLogger.getAlert());
						eventList.add(eventLogger);

						if (eventList.size() == BATCH_SIZE) {

							// write into DB
							batchInsert(con, eventList, tableName);

							//eventList.forEach(e -> System.out.println(e.getId()));
							eventList.clear();

						}

						eventsMap.remove(evt1.getId());

					} else {

						eventsMap.putIfAbsent(evt1.getId(), evt1);
					}

					if (!eventList.isEmpty()) {
						batchInsert(con, eventList, tableName);
						//eventList.forEach(e -> System.out.println(e.getId()));
						eventList.clear();
					}

				} catch (JsonParseException e) {
					logger.error("Error in parsing JSON events" + e.getMessage());
				} catch (JsonMappingException e) {
					logger.error("Error in processing JSON events" + e.getMessage());
				} catch (IOException e) {
					logger.error("IO Exception" + e.getMessage());
				} catch (SQLException e) {
					logger.error("Error in inserting into table " + e.getMessage());
				}
				//System.out.println(line);
			});
		} catch (FileNotFoundException e) {
			logger.error("Unable to find file " + e.getMessage());
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				logger.error("Error in closing reader" + e.getMessage());
			}
		}

	}

	/**
	 * batch insert into database
	 * 
	 * @param con
	 * @param eventList
	 * @param tableName
	 * @throws SQLException
	 */
	public int batchInsert(Connection con, List<EventsLogger> eventList, String tableName) throws SQLException {

		PreparedStatement preparedStatement;
		int size = 0;

		con.setAutoCommit(false);
		preparedStatement = con.prepareStatement(
				"INSERT INTO " + tableName + " (id, event_duartion,type,host,alert) VALUES(?,?,?,?,?)");

		if (eventList != null && !eventList.isEmpty()) {
			size = eventList.size();
			eventList.forEach(event -> {
				try {
					preparedStatement.setString(1, event.getId());
					preparedStatement.setString(2, event.getDuration());
					preparedStatement.setString(3, event.getType());
					preparedStatement.setString(4, event.getHost());
					preparedStatement.setString(5, event.getAlert());
					preparedStatement.addBatch();
				} catch (SQLException e) {
					logger.error("Error in inserting into table " + e.getMessage());
				}

			});
			preparedStatement.executeBatch();
			con.commit();
		}
		return size;
	}

}