package model.configuration;

import java.util.HashMap;
import java.util.Map;

public class Config {

	private static Config instance = new Config();
	
	private static final String CONFIG_FILE = "project.properties";
	
	public static Config getInstance() {
		return instance;
	}
	
	private KeyValueConfiguration generalConfguration;
	private Map<String, KeyValueConfiguration> cachedKeyValueConfig;
	private Map<String, SimpleListConfiguration> cachedSimpleListConfig;
	
	public Config() {
		cachedSimpleListConfig = new HashMap<String, SimpleListConfiguration>();
		cachedKeyValueConfig = new HashMap<String, KeyValueConfiguration>();
		generalConfguration = new KeyValueConfiguration(CONFIG_FILE);
	}
	
	public KeyValueConfiguration getGeneralConfig() {
		return generalConfguration;
	}
	
	public String getConfigResourcePath(String name) {
		String fileName = generalConfguration.get(name);
		String resourcePath = generalConfguration.get("specific_conf_dir");
		if(fileName == null){
			return null;
		}
		return resourcePath + fileName;
	}
	
	public SimpleListConfiguration getSimpleListConfig(String name) {
		String fileName = generalConfguration.get(name);
		if (fileName == null) {
			throw new IllegalStateException(name + "is not defined!");
		}
		SimpleListConfiguration simple = cachedSimpleListConfig.get(fileName);
		if (simple != null) {
			return simple;
		}
		String resourcePath = generalConfguration.get("specific_conf_dir");
		String path = resourcePath + fileName;
		simple = new SimpleListConfiguration(path);
		cachedSimpleListConfig.put(fileName, simple);
		return simple;
	}
	
	public KeyValueConfiguration getKeyValueConfig(String name) {
		String fileName = generalConfguration.get(name);
		if (fileName == null) {
			throw new IllegalStateException(name + "is not defined!");
		}
		KeyValueConfiguration keyValue = cachedKeyValueConfig.get(fileName);
		if (keyValue != null) {
			return keyValue;
		}
		String resourcePath = generalConfguration.get("specific_conf_dir");
		resourcePath += fileName;
		keyValue = new KeyValueConfiguration(resourcePath);
		cachedKeyValueConfig.put(fileName, keyValue);
		return keyValue;
	}

	public void update(String name) {
		String fileName = generalConfguration.get(name);
		KeyValueConfiguration keyValue = cachedKeyValueConfig.get(fileName);
		if (keyValue != null) {
			keyValue.update();
		}
		SimpleListConfiguration simple = cachedSimpleListConfig.get(fileName);
		if (simple != null) {
			simple.update();
		}
	}

}
