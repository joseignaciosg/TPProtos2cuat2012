package parser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

public class HeaderReader {

	private static Logger logger = Logger.getLogger(HeaderReader.class);
	private String mail_string;
	private File mail_file;

	public HeaderReader(final String mail) {
		this.mail_string = mail;
	}

	public HeaderReader(final File mail) {
		this.mail_file = mail;
	}

	public String getHeaders() throws IOException {
		final StringBuilder builder = new StringBuilder();
		// final BufferedReader reader = new BufferedReader(new FileReader(
		// this.mail_string));
		final BufferedReader reader = new BufferedReader(new FileReader(
				this.mail_file));
		final String boundary = this.getBoundary();
		logger.debug("Reading boundary:" + boundary);

		String line;
		do {
			line = reader.readLine();
			builder.append(reader.readLine() + "\r\n");
		} while (!Pattern.matches(".*--" + boundary + ".*", line));
		reader.close();
		logger.debug("Reading headers:");
		return builder.toString();
	}

	private String getBoundary() throws IOException {
		// final BufferedReader reader = new BufferedReader(new FileReader(
		// this.mail_string));
		final BufferedReader reader = new BufferedReader(new FileReader(
				this.mail_file));
		// final Pattern p = Pattern.compile(".*boundary//.*");
		String line;
		final Matcher m;
		do {
			line = reader.readLine();
			final boolean isMatch = Pattern.matches(".*boundary.*", line);
			// m = p.matcher(line);
			// if (m.matches()) {
			if (isMatch) {
				reader.close();
				return line.split("\"")[1];
			}
		} while (line != null);
		reader.close();
		return null;
	}
}
