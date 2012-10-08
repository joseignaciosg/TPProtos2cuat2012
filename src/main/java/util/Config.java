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
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(CONFIG_FILE);
		properties.load(in);
		in.close();
	}
	
	private Config(String file) throws IOException {
		properties = new Properties();
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(file);
		properties.load(in);
		in.close();
	}
	
	public Config getConfig(String key) {
		String file = Config.getInstance().get(key);
		try {
			String dir = properties.getProperty("specific_conf_dir");
			return new Config(dir + file);
		} catch (IOException e) {
			 throw new IllegalArgumentException(file + " does not exists");
		}
	}
	
	public String get(String key) {
		return properties.getProperty(key);
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
