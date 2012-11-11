package model.configuration;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import model.util.StringUtil;

public class ConfigUtil {

	private static ConfigUtil instance = new ConfigUtil();
	
	private static final String CONFIG_FILE = "project.properties";
	
	public static ConfigUtil getInstance() {
		return instance;
	}
	
	private String configurationsFolder;
	private KeyValueConfiguration mainConfguration;
	private Map<String, KeyValueConfiguration> cachedKeyValueConfig;
	private Map<String, SimpleListConfiguration> cachedSimpleListConfig;
	
	private ConfigUtil() {
		cachedSimpleListConfig = new HashMap<String, SimpleListConfiguration>();
		cachedKeyValueConfig = new HashMap<String, KeyValueConfiguration>();
		mainConfguration = new KeyValueConfiguration(CONFIG_FILE);
		configurationsFolder = System.getProperty("configurationsFolder");
		if (StringUtil.empty(configurationsFolder)) {
			throw new IllegalArgumentException("System property " + configurationsFolder + " is not set!");
		}
		configurationsFolder = new File(configurationsFolder).getAbsolutePath() + "/";
	}
	
	public KeyValueConfiguration getMainConfig() {
		return mainConfguration;
	}
	
	public String getConfigPath(String name) {
		String fileName = mainConfguration.get(name);
		if (fileName == null) {
			return null;
		}
		String resourcePath = mainConfguration.get("specific_conf_dir");
		return configurationsFolder + resourcePath + fileName;
	}
	
	public SimpleListConfiguration getSimpleListConfig(String name) {
		String fileName = mainConfguration.get(name);
		if (fileName == null) {
			throw new IllegalStateException(name + "is not defined!");
		}
		SimpleListConfiguration simple = cachedSimpleListConfig.get(fileName);
		if (simple != null) {
			return simple;
		}
		String resourcePath = mainConfguration.get("specific_conf_dir");
		String path = resourcePath + fileName;
		simple = new SimpleListConfiguration(path);
		cachedSimpleListConfig.put(fileName, simple);
		return simple;
	}
	
	public KeyValueConfiguration getKeyValueConfig(String name) {
		String fileName = mainConfguration.get(name);
		if (fileName == null) {
			throw new IllegalStateException(name + "is not defined!");
		}
		KeyValueConfiguration keyValue = cachedKeyValueConfig.get(fileName);
		if (keyValue != null) {
			return keyValue;
		}
		String resourcePath = mainConfguration.get("specific_conf_dir");
		resourcePath += fileName;
		keyValue = new KeyValueConfiguration(resourcePath);
		cachedKeyValueConfig.put(fileName, keyValue);
		return keyValue;
	}

	public void update(String name) {
		String fileName = mainConfguration.get(name);
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
