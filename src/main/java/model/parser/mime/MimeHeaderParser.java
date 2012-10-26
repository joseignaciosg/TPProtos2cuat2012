package model.parser.mime;

import java.io.IOException;
import java.util.Scanner;

import model.mail.Mail;

import org.apache.log4j.Logger;

public class MimeHeaderParser {

	private static Logger logger = Logger.getLogger(MimeHeaderParser.class);
	
	public MimeHeaderParser() {
	}
	
	public void parse(Scanner scanner, Mail mail) throws IOException {
		logger.debug("Reading mail headers:");
		String lastReadLine = scanner.nextLine();
		while (scanner.hasNextLine()) {
			lastReadLine = createHeader(lastReadLine, scanner, mail);
			if (lastReadLine.equals("")) {
			    	mail.appendTransformedLine("");
				break;
			}
		}
		String boundary = mail.getBoundaryKey();
		if (boundary == null) {
			throw new IllegalStateException("boundary header could not be parsed");
		}
	}
	
	private String createHeader(String lastReadLine, Scanner scanner, Mail mail) {
		boolean endOfHeader;
		String line = lastReadLine;
		mail.appendTransformedLine(line);
		do {
			endOfHeader = true;
			lastReadLine = scanner.nextLine();
			if (lastReadLine.startsWith(" ") || lastReadLine.startsWith("\t")
				|| lastReadLine.startsWith(".")) {
				line += lastReadLine.trim();
				mail.appendTransformedLine(lastReadLine);
				endOfHeader = false;
			}
		} while(!endOfHeader);
		try {
			MimeHeader header = new MimeHeader(line);
			mail.addHeaders(header);
			logger.info("Parsed header => " + header);
		} catch (IllegalArgumentException e) {
			System.out.println("Inavlid header: " + line + ". Ignoring...");
		}
		return lastReadLine;
	}

}
