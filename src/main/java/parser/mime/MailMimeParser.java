package parser.mime;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import model.Mail;

import org.apache.log4j.Logger;

public class MailMimeParser {

	protected static final Logger logger = Logger.getLogger(MailMimeParser.class);
	
	private MimeHeaderParser headerParser;
		
	public MailMimeParser() {
		headerParser = new MimeHeaderParser();
	}
	
	public Mail parse(File mimeFile) throws IOException {
		return parse(mimeFile, mimeFile.length());
	}
	
	public Mail parse(File mimeFile, long sizeInBytes) throws IOException {
		Mail mail = new Mail();
		mail.setSizeInBytes(sizeInBytes);
		Scanner scanner = new Scanner(mimeFile);
		headerParser.parse(scanner, mail);
		parse(scanner, mail.getBoundaryKey(), mail);
		scanner.close();
		return mail;
	}

	private void parse(Scanner scanner, String boundary, Mail mail) {
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.equals("--" + boundary)) {
				logger.info("START : " + boundary);
				// start of a segment
				parseSubBoundaryHeader(scanner, mail);
			} else if (line.equals("--" + boundary + "--")) {
				logger.info("END : " + boundary);
				// end of mail file
				return;
			}
		}
	}

	private void parseSubBoundaryHeader(Scanner scanner, Mail mail) {
		MimeHeader contentType = null;
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			if (line.equals("")) {
				break;
			}
			MimeHeader header = new MimeHeader(line);
			mail.addAttachmentsExtension(header.getValue());
			if (header.getKey().equals("Content-Type")) {
				contentType = header;
			}
		}
		if (contentType != null) {
			String subBoundary = contentType.getExtraValue("boundary");
			if (subBoundary != null) {				
				logger.info("Sub boundary " + subBoundary + " found. Recursively parsing.");
				parse(scanner, subBoundary, mail);
			}
		}
	}
}
