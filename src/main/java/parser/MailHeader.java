package parser;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;

public class MailHeader {

	private static Logger logger = Logger.getLogger(MailHeader.class);
	
	private Map<String, String> headers;
	
	private String lastKey;
	
	public MailHeader(File mail) {
		headers = new HashMap<String, String>();
		try {
			parse(mail);
		} catch (IOException e) {
			throw new IllegalStateException("Could not parse headers from mail");
		}
	}

	public String getHeader(String key) {
		return headers.get(key);
	}
	
	public String getBoundary() {
		return headers.get("boundary");
	}
	
	private void parse(File mail) throws IOException {
		logger.debug("Reading headers:");
		Scanner scanner = new Scanner(mail);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			parse(line);
			if (line.equals("")) {
				break;
			}
		}
		scanner.close();
		if (!headers.containsKey("boundary")) {
			throw new IllegalStateException("boundary header could not be parsed");
		}
	}

	private void parse(String line) {
		if (line.startsWith(".")) {
			return;
		}
		if (line.startsWith("\t\t")) {
			String lastValue = headers.get(lastKey);
			headers.put(lastKey, lastValue + line);
			return;
		}
		String[] parts = line.split(";");
		if (parts.length >= 1) {
			saveHeader(parts[0]);
			for (int i = 1; i < parts.length; i++) {
				parseHeadersCont(parts[i]);
			}
		}
	}

	private void saveHeader(String s) {
		int splitIndex = s.indexOf(":");
		if (splitIndex != -1) {
			String key = s.substring(0, splitIndex);
			String value = s.substring(splitIndex + 1);
			headers.put(key, value);
		}
	}
	
	private void parseHeadersCont(String s) {
		String[] parts = s.split("=");
		if (parts.length == 2) {
			String key = parts[0].trim();
			headers.put(key, parts[1].trim());
		}
	}
	
	public Map<String, String> getHeader() {
		return headers;
	}
}
