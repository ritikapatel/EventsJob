package com.events.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.events.constants.ConfigConstants;

/**
 * @author
 *
 */
public class PropertyLoader implements ConfigConstants {

	private static final Logger logger = LoggerFactory.getLogger(PropertyLoader.class);

	static Map<String, String> propertiesMap = new HashMap<String, String>();

	/**
	 * Load the property file and store it's content in map as key/value pair
	 */
	public Properties getPropertyLoader() {
		Properties properties = new Properties();
		InputStream input = null;

		try {

			input = getClass().getClassLoader().getResourceAsStream(CONFIG_PROPERTIES);

			// load properties file
			properties.load(input);

			input.close();

			Enumeration<Object> enuKeys = properties.keys();
			while (enuKeys.hasMoreElements()) {
				String key = (String) enuKeys.nextElement();
				String value = properties.getProperty(key);

				if (key != null && !"".equals(key.trim()))
					propertiesMap.put(key, value);
			}

		} catch (IOException ex) {
			logger.error("Error in reading properties file " + ex.getMessage());
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					logger.error("Error in closing connection " + e.getMessage());
				}
			}
		}
		return properties;

	}

	/**
	 * Read the file
	 * 
	 * @param propertyName
	 * @return String
	 */
	public String get(String propertyName) {
		final String moduleName = "get";
		logger.debug("- " + moduleName + " - propertyName = " + propertyName + ", value = "
				+ propertiesMap.get(propertyName));
		return (propertyName != null && !"".equals(propertyName.trim()) && propertiesMap.containsKey(propertyName))
				? propertiesMap.get(propertyName)
				: null;
	}
}