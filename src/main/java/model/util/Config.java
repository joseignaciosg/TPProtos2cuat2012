package model.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

public class Config {
	
	private static Logger logger = Logger.getLogger(Config.class);
	
	private static final String CONFIG_FILE = "project.properties";
	
	private static Config instance;
	private Map<String, Config> specificConfigs;
	
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
		specificConfigs = new HashMap<String, Config>();
		properties = new Properties();
		InputStream in = IOUtil.getStream(CONFIG_FILE);
		properties.load(in);
		in.close();
	}
	
	private Config(String file) throws IOException {
		properties = new Properties();
		InputStream in = IOUtil.getStream(file);
		properties.load(in);
		in.close();
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
	
	public synchronized Config getConfig(String fileNamekey) {
		String file = Config.getInstance().get(fileNamekey);
		if (specificConfigs.containsKey(file)) {
			return specificConfigs.get(file);
		}
		Config specific = getSpecific(file);
		specificConfigs.put(file, specific);
		return specific;
	}
	
	public synchronized void update(String file) {
		if (specificConfigs.containsKey(file)) {
			Config old = specificConfigs.get(file);
			old.properties = getSpecific(file).properties;
			logger.info("Updating contents on memory for configuration file: " + file);
		}
	}
	
	private Config getSpecific(String file) {
		try {
			String dir = properties.getProperty("specific_conf_dir");
			Config specific = new Config(dir + file);
			specificConfigs.put(file, specific);
			return specific;
		} catch (IOException e) {
			 throw new IllegalArgumentException(file + " does not exists");
		}
	}
}
