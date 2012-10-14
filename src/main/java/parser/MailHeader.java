package parser;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class MailHeader {

	private static Logger logger = Logger.getLogger(MailHeader.class);
	private File mail;

	public MailHeader(File mail) {
		this.mail = mail;
	}

	public String getHeaders() throws IOException {
		StringBuilder builder = new StringBuilder();
		// final BufferedReader reader = new BufferedReader(new FileReader(
		// this.mail_string));
		Scanner scanner = new Scanner(mail);
		final String boundary = this.getBoundary();
		logger.debug("Reading boundary:" + boundary);
		while (scanner.hasNextLine()) {
			final String line = scanner.nextLine();
			if (!Pattern.matches(".*--" + boundary + ".*", line)) {
				builder.append(line + "\r\n");
			} else {
				break;
			}
		}
		scanner.close();
		logger.debug("Reading headers:");
		return builder.toString();
	}

	public String getHeader(String name) {
		return null;
	}

	private String getBoundary() throws IOException {
		Scanner scanner = new Scanner(this.mail);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			boolean isMatch = Pattern.matches(".*boundary.*", line);
			if (isMatch) {
				scanner.close();
				return line.split("\"")[1];
			}
		}
		scanner.close();
		return null;
	}
}
