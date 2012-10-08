package server.start;

import java.io.IOException;
import java.nio.file.StandardWatchEventKinds;

import util.Config;
import worker.watcher.ConfigWatcherCallback;
import worker.watcher.DirectoryWatcher;

public class ProxyInitializer {

	public static void main(String[] args) {
		new ProxyInitializer().initialize();
	}
	
	public void initialize() {
		System.out.println("Initializing proxy.....");
		new ServerInitializer().initialize("server.init");
		initFileWatcher();
		System.out.println("Proxy Started succesfully!");
	}
	
	private void initFileWatcher() {
		try {
			boolean recursive = false;
			String[] dirs = {Config.getInstance().get("specific_conf_dir")};
			DirectoryWatcher watcher = new DirectoryWatcher(dirs, recursive);
			watcher.suscribe(StandardWatchEventKinds.ENTRY_CREATE, 
				new ConfigWatcherCallback("Modification Listener"));
			new Thread(watcher).start();
		} catch (IOException e) {
			throw new IllegalStateException("Could not initialize file watcher");
		}
	}
}
