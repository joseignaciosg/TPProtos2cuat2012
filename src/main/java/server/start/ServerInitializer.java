package server.start;

import java.util.Scanner;

import org.apache.log4j.Logger;

import server.AbstractSockectServer;
import server.GenericServer;
import util.IOUtil;
import util.StringUtil;

public class ServerInitializer {

	private static Logger logger = Logger.getLogger(ProxyInitializer.class);
	
	public void initialize(String fileName) {
		Scanner scanner = IOUtil.createScanner(fileName);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (StringUtil.empty(line) || line.startsWith("#")) {
				continue;
			}
			String[] parts = line.split(",");
			if (parts.length == 2) {
				String className = parts[0].trim();
				int port = Integer.parseInt(parts[1].trim());
				boolean initialized = initialize(port, className);
				if (!initialized) {
					printStatus("Ignoring invalid line: " + line, true);
				} else {
					printStatus("server: " + className + ". Port: " + port, false);
				}
			} else {
				printStatus("Ignoring invalid line: " + line, true);
			}
		}
		scanner.close();
	}
	
	private boolean initialize(int port, String className) {
		try {
			Class<? extends AbstractSockectServer> clazz;
			if (AbstractSockectServer.class.isAssignableFrom(Class.forName(className))) {				
				clazz = (Class<? extends AbstractSockectServer>) Class.forName(className);
				new Thread(new GenericServer(port, clazz)).start();
				return true;
			} else {
				logger.error(className + " does not extend " + AbstractSockectServer.class);
				return false;
			}
		} catch (ClassNotFoundException e) {
			logger.error("Could not find class " + className);
			return false;
		}
	}
	
	private void printStatus(String status, boolean isError) {
		if (isError) {
			logger.error("[ERROR]\t " + status);
		} else {
			logger.info("[OK]\t " + status);
		}
	}
}
