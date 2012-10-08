package worker.watcher;

import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static java.nio.file.StandardWatchEventKinds.OVERFLOW;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import util.Config;

public class DirectoryWatcher implements Runnable {

	private final WatchService watcher;
	private final Map<WatchKey, Path> keys;
	private final boolean recursive;
	private Map<String, Set<FileWatcherCallback>> callbacks;

	private boolean trace = false;

	@SuppressWarnings("unchecked")
	private static <T> WatchEvent<T> cast(WatchEvent<?> event) {
		return (WatchEvent<T>) event;
	}

	public DirectoryWatcher(String[] directories, boolean recursive) throws IOException {
		callbacks = new HashMap<String, Set<FileWatcherCallback>>();
		Path dir = getDirectoriesPath(directories);
		this.watcher = FileSystems.getDefault().newWatchService();
		this.keys = new HashMap<WatchKey, Path>();
		this.recursive = recursive;
		if (recursive) {
			System.out.format("Scanning %s ...\n", dir);
			registerAll(dir);
			System.out.println("Done.");
		} else {
			register(dir);
		}
		this.trace = true;
	}
	
	public void suscribe(Kind<?> eventType, FileWatcherCallback callback) {
		String name = eventType.name();
		Set<FileWatcherCallback> registered = callbacks.get(name);
		if (registered == null) {
			registered = new HashSet<FileWatcherCallback>();
			callbacks.put(name, registered);
		}
		registered.add(callback);
	}
	
	private Path getDirectoriesPath(String[] directories) {
		if (directories.length == 1) {
			return Paths.get(getClass().getClassLoader().getResource(directories[0]).getPath());
		}
		String[] paths = new String[directories.length - 1];
		for (int i = 1; i < paths.length; i++) {
			paths[i - 1] = getClass().getClassLoader().getResource("conf/").getPath();
		}
		return Paths.get(directories[0], paths);
	}

	private void register(Path dir) throws IOException {
		WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
		if (trace) {
			Path prev = keys.get(key);
			if (prev == null) {
				System.out.format("register: %s\n", dir);
			} else {
				if (!dir.equals(prev)) {
					System.out.format("update: %s -> %s\n", prev, dir);
				}
			}
		}
		keys.put(key, dir);
	}

	private void registerAll(final Path start) throws IOException {
		// register directory and sub-directories
		Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir,
					BasicFileAttributes attrs) throws IOException {
				register(dir);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	@Override
	public void run() {
		while (!Thread.interrupted()) {
			try {
				int sleepTime = Config.getInstance().getInt("file_watcher_check_rate");
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
			}
			// wait for key to be signalled
			WatchKey key;
			try {
				key = watcher.take();
			} catch (InterruptedException x) {
				return;
			}
			Path dir = keys.get(key);
			if (dir == null) {
				System.err.println("WatchKey not recognized!!");
				continue;
			}
			for (WatchEvent<?> event : key.pollEvents()) {
				WatchEvent.Kind kind = event.kind();
				// TBD - provide example of how OVERFLOW event is handled
				if (kind == OVERFLOW) {
					continue;
				}
				// Context for directory entry event is the file name of entry
				WatchEvent<Path> ev = cast(event);
				Path name = ev.context();
				Path child = dir.resolve(name);
				notifyAllSuscribers(event.kind(), child);
				// if directory is created, and watching recursively, then
				// register it and its sub-directories
				if (recursive && (kind == ENTRY_CREATE)) {
					try {
						if (Files.isDirectory(child, NOFOLLOW_LINKS)) {
							registerAll(child);
						}
					} catch (IOException x) {
						// ignore to keep sample readbale
					}
				}
			}
			// reset key and remove from set if directory no longer accessible
			boolean valid = key.reset();
			if (!valid) {
				keys.remove(key);
				// all directories are inaccessible
				if (keys.isEmpty()) {
					break;
				}
			}
		}
	}
	
	private void notifyAllSuscribers(Kind<?> kind, Path child) {
		if (child.toString().contains(".goutputstream")) {
			return;
		}
		// System.out.format("%s: %s\n", kind.name(), child);
		Set<FileWatcherCallback> registered = callbacks.get(kind.name());
		if (registered != null) {
			for(FileWatcherCallback suscriber : registered) {
				suscriber.notifyEvent(child);
			}
		}
	}

}
