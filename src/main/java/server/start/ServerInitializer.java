package server.start;

import java.io.InputStream;
import java.util.Scanner;

import server.AbstractSockectServer;
import server.GenericServer;
import util.StringUtil;

public class ServerInitializer {

	public void initialize(String fileName) {
		InputStream in = getClass().getClassLoader().getResourceAsStream(fileName);
		Scanner scanner = new Scanner(in);
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
				System.out.println(className + " does not extend " + AbstractSockectServer.class);
				return false;
			}
		} catch (ClassNotFoundException e) {
			System.out.println("Could not find class " + className);
			return false;
		}
	}
	
	private void printStatus(String status, boolean isError) {
		if (isError) {
			System.out.println("[ERROR]\t " + status);
		} else {
			System.out.println("[OK]\t " + status);
		}
	}
}
