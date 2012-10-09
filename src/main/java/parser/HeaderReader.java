package parser;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;
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
	final Scanner scanner = new Scanner(this.mail_file);
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

    private String getBoundary() throws IOException {
	final Scanner scanner = new Scanner(this.mail_file);
	while (scanner.hasNextLine()) {
	    final String line = scanner.nextLine();
	    final boolean isMatch = Pattern.matches(".*boundary.*", line);
	    if (isMatch) {
		scanner.close();
		return line.split("\"")[1];
	    }
	}
	scanner.close();
	return null;
    }
}
