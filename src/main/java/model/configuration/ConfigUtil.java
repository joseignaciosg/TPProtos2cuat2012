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
		configurationsFolder = System.getProperty("configurationsFolder");
		if (StringUtil.empty(configurationsFolder)) {
			throw new IllegalArgumentException("System property " + configurationsFolder + " is not set!");
		}
		configurationsFolder = new File(configurationsFolder).getAbsolutePath() + "/";
		cachedSimpleListConfig = new HashMap<String, SimpleListConfiguration>();
		cachedKeyValueConfig = new HashMap<String, KeyValueConfiguration>();
		mainConfguration = new KeyValueConfiguration(configurationsFolder + CONFIG_FILE);
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
		String path = getConfigPath(name);
		if (path == null) {
			throw new IllegalStateException(name + "is not defined!");
		}
		SimpleListConfiguration simple = cachedSimpleListConfig.get(path);
		if (simple == null) {
			simple = new SimpleListConfiguration(path);
			cachedSimpleListConfig.put(path, simple);
		}
		return simple;
	}
	
	public KeyValueConfiguration getKeyValueConfig(String name) {
		String path = getConfigPath(name);
		if (path == null) {
			throw new IllegalStateException(name + "is not defined!");
		}
		KeyValueConfiguration keyValue = cachedKeyValueConfig.get(path);
		if (keyValue == null) {
			keyValue = new KeyValueConfiguration(path);
			cachedKeyValueConfig.put(path, keyValue);
		}
		return keyValue;
	}

	public void update(String name) {
		String path = getConfigPath(name);
		KeyValueConfiguration keyValue = cachedKeyValueConfig.get(path);
		if (keyValue != null) {
			keyValue.update();
		}
		SimpleListConfiguration simple = cachedSimpleListConfig.get(path);
		if (simple != null) {
			simple.update();
		}
	}

}
