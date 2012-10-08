package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
	
	private static final String CONFIG_FILE = "project.properties";
	
	private static Config instance;
	
	public static Config getInstance() {
		if (instance == null) {
			try {
				instance = new Config();
			} catch (IOException e) {
				System.out.println("Configurations for " + CONFIG_FILE + " could not be loaded.");
				System.exit(1);
			}
		}
		return instance;
	}

	private Properties properties;
	
	private Config() throws IOException {
		properties = new Properties();
		InputStream in = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE);
		properties.load(in);
		in.close();
	}
	
	public String get(String key) {
		String value = properties.getProperty(key);
		if (value == null) {
			 throw new IllegalArgumentException(key + " is not set");
		}
		return value;
	}
	
	public int getInt(String key) {
		return Integer.parseInt(properties.getProperty(key));
	}
	
	public boolean getBoolean(String key) {
		return Boolean.valueOf(properties.getProperty(key));
	}
	
	public void put(String key, String value) {
		properties.put(key, value);
	}
	
}
