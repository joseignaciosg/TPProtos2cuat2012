package model.configuration;

import java.util.Properties;

import model.util.IOUtil;

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

	public void update() {
		try {
			properties.clear();
			properties.load(IOUtil.getStream(path));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
