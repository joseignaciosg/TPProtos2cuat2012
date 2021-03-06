package model.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class KeyValueConfiguration {

	private String path;
	private Properties properties;

	public KeyValueConfiguration(String path) {
		this.path = path;
		properties = new Properties();
		update();
	}

	public String get(String key) {
		return (String) properties.get(key);
	}

	public int getInt(String key) {
		return Integer.valueOf(get(key));
	}

	public boolean getBoolean(String key) {
		return Boolean.valueOf(get(key));
	}
	
	public long getLong(String key) {
		return Long.valueOf(get(key));
	}

	public void update() {
		try {
			properties.clear();
			properties.load(new FileInputStream(new File(path)));
		} catch (Exception e) {
			throw new IllegalArgumentException(path + " does not exists.");
		}
	}
}
