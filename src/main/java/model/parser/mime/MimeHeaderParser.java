package model.parser.mime;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Map.Entry;
import java.util.Scanner;

import model.mail.Mail;
import model.mail.MailTransformer;

import org.apache.log4j.Logger;

public class MimeHeaderParser {

	private static Logger logger = Logger.getLogger(MimeHeaderParser.class);

	public void parse(Scanner scanner, Mail mail, MailTransformer transformer) throws IOException {
		logger.debug("Reading mail headers:");
		String lastReadLine = scanner.nextLine();
		while (scanner.hasNextLine()) {
			lastReadLine = createHeader(lastReadLine, scanner, mail, transformer);
			if (lastReadLine.equals("")) {
				break;
			}
		}
		String boundary = mail.getBoundaryKey();
		if (boundary == null) {
			throw new IllegalStateException("boundary header could not be parsed");
		}
	}

	public void writeHeaders(Mail mail, FileWriter writer) throws IOException{
		for(Entry<String, MimeHeader> entry: mail.getHeaders().entrySet()){
			writer.append(entry.getValue() + "\r\n");
		}
		writer.append("\r\n");
	}
	
	private String createHeader(String lastReadLine, Scanner scanner, Mail mail, MailTransformer tranformer) throws IOException {
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
			logger.debug("Parsed header => " + header);
		} catch (IllegalArgumentException e) {
			logger.error("Inavlid header: " + line + ". Ignoring...");
		}
		return lastReadLine;
	}

}
