package service.start;

import java.io.IOException;
import java.nio.file.StandardWatchEventKinds;

import model.util.Config;

import org.apache.log4j.Logger;

import service.worker.watcher.ConfigWatcherCallback;
import service.worker.watcher.DirectoryWatcher;

public class ProxyInitializer {

	private static Logger logger = Logger.getLogger(ProxyInitializer.class);

	public static void main(final String[] args) {
		new ProxyInitializer().initialize();
	}

	public void initialize() {
		logger.trace("Initializing proxy.....");
		new ServerInitializer().initialize("server.init");
		this.initFileWatcher();
		logger.trace("Proxy Started succesfully!");
	}

	private void initFileWatcher() {
		try {
			final String[] dirs = { Config.getInstance().get("specific_conf_dir") };
			final DirectoryWatcher watcher = new DirectoryWatcher(dirs, false);
			watcher.suscribe(StandardWatchEventKinds.ENTRY_CREATE,
					new ConfigWatcherCallback("Creation Listener"));
			watcher.suscribe(StandardWatchEventKinds.ENTRY_MODIFY,
					new ConfigWatcherCallback("Modification Listener"));

			new Thread(watcher).start();
		} catch (final IOException e) {
			throw new IllegalStateException("Could not initialize file watcher");
		}
	}
}
