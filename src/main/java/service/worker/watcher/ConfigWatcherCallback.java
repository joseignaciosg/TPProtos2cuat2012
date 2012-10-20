package service.worker.watcher;

import java.nio.file.Path;

import model.util.Config;

import org.apache.log4j.Logger;


public class ConfigWatcherCallback implements FileWatcherCallback {

	private static Logger logger = Logger.getLogger(ConfigWatcherCallback.class);
	
	private String name;
	
	public ConfigWatcherCallback(String name) {
		this.name = name;
	}
	
	@Override
	public void notifyEvent(Path path) {
		if (path.toString().endsWith(".conf")) {
			String fileName = path.getFileName().toString();
			logger.trace(fileName + " has been updated. Notifying Config class to update");
			Config.getInstance().update(fileName);
		}
	}

}
