package api.automation.usermanagement.configmanager;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

//Used for reading the Properties file
public final class ConfigManager {
	private static final Properties PROPERTIES = loadProperties();

	private ConfigManager() {
	}

	public static String get(String key) {
		return PROPERTIES.getProperty(key);
	}

	public static String required(String key) {
		String value = get(key);// Gets the value of the key

		if (value == null || value.isBlank()) {
			throw new IllegalStateException("Missing required property: " + key);
		}

		return value;
	}

	private static Properties loadProperties() {
		Properties properties = new Properties();

		try (InputStream input = new java.io.FileInputStream("DataSource/config.property"))
		{

			properties.load(input);
			return properties;
		} catch (IOException exception) {
			throw new IllegalStateException("Unable to load configuration.", exception);
		}
	}
}
