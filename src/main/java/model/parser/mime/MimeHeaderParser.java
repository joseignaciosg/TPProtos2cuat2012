package model.parser.mime;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import model.mail.Mail;
import model.mail.MailTransformer;

import org.apache.log4j.Logger;

public class MimeHeaderParser {

	private static Logger logger = Logger.getLogger(MimeHeaderParser.class);

	public void parse(Scanner scanner, Mail mail, FileWriter writer, MailTransformer transformer) throws IOException {
		logger.debug("Reading mail headers:");
		String lastReadLine = scanner.nextLine();
		while (scanner.hasNextLine()) {
			lastReadLine = createHeader(lastReadLine, scanner, mail, writer, transformer);
			if (lastReadLine.equals("")) {
				writer.append(lastReadLine + "\r\n");
				break;
			}
		}
		String boundary = mail.getBoundaryKey();
		if (boundary == null) {
			throw new IllegalStateException("boundary header could not be parsed");
		}
	}

	private String createHeader(String lastReadLine, Scanner scanner, Mail mail, FileWriter writer, MailTransformer tranformer) throws IOException {
		boolean endOfHeader;
		String line = lastReadLine;
		do {
			endOfHeader = true;
			lastReadLine = scanner.nextLine();
			if (lastReadLine.startsWith("\t") || lastReadLine.startsWith(" ") || lastReadLine.startsWith(".")) {
				line += "\r\n" + lastReadLine;
				endOfHeader = false;
			}
		} while (!endOfHeader);
		try {
			MimeHeader header = new MimeHeader(line);
			mail.addHeaders(header);
			writer.append(line + "\r\n");
			logger.debug("Parsed header => " + header);
		} catch (IllegalArgumentException e) {
			logger.error("Inavlid header: " + line + ". Ignoring...");
		}
		return lastReadLine;
	}

}
