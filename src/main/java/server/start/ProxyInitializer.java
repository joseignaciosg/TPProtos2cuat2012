package server.start;

import java.io.IOException;
import java.nio.file.StandardWatchEventKinds;

import org.apache.log4j.Logger;

import util.Config;
import worker.watcher.ConfigWatcherCallback;
import worker.watcher.DirectoryWatcher;

public class ProxyInitializer {

	private static Logger logger = Logger.getLogger(ProxyInitializer.class);

	public static void main(String[] args) {
		new ProxyInitializer().initialize();
	}

	public void initialize() {
		logger.trace("Initializing proxy.....");
		new ServerInitializer().initialize("server.init");
		initFileWatcher();
		logger.trace("Proxy Started succesfully!");
	}

	private void initFileWatcher() {
		try {
			String[] dirs = { Config.getInstance().get("specific_conf_dir") };
			DirectoryWatcher watcher = new DirectoryWatcher(dirs, false);
			watcher.suscribe(StandardWatchEventKinds.ENTRY_CREATE,
					new ConfigWatcherCallback("Modification Listener"));
			new Thread(watcher).start();
		} catch (IOException e) {
			throw new IllegalStateException("Could not initialize file watcher");
		}
	}
}
