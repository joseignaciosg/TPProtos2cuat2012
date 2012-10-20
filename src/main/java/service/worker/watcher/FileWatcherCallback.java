package service.worker.watcher;

import java.nio.file.Path;

public interface FileWatcherCallback {

	void notifyEvent(Path path);
	
}
