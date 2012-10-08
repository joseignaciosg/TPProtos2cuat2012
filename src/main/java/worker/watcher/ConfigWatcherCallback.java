package worker.watcher;

import java.nio.file.Path;

import util.Config;

public class ConfigWatcherCallback implements FileWatcherCallback {

	private String name;
	
	public ConfigWatcherCallback(String name) {
		this.name = name;
	}
	
	@Override
	public void notifyEvent(Path path) {
		if (path.toString().endsWith(".conf")) {
			String fileName = path.getFileName().toString();
			Config.getInstance().update(fileName);
		}
	}

}
