package model.parser.mime;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import model.mail.Mail;
import model.mail.MailTransformer;

import org.apache.log4j.Logger;

public class MailMimeParser {

	protected static final Logger logger = Logger.getLogger(MailMimeParser.class);

	private MimeHeaderParser headerParser;

	public MailMimeParser() {
		headerParser = new MimeHeaderParser();
	}

	public Mail parse(File mimeFile, MailTransformer transformer) throws IOException {
		return parse(mimeFile, mimeFile.length(), transformer);
	}

	public Mail parse(File mimeFile, long sizeInBytes, MailTransformer transformer) throws IOException {
		Mail mail = new Mail(mimeFile);
		File transformedFile = File.createTempFile("transformed_", mimeFile.getName());
		mail.setSizeInBytes(sizeInBytes);
		Scanner scanner = new Scanner(mimeFile);
		FileWriter transWriter = new FileWriter(transformedFile);
		parseBoundary(scanner, mail);
		scanner.close();
		return mail;
	}

	private void parseBoundary(Scanner scanner, Mail mail) throws IOException {
		headerParser.parse(scanner, mail);
		// TODO: hacer con los header todas las transformaciones pertinentes con el objeto MailTransformer
		String boundary = mail.getBoundaryKey();
		String boundaryLine = scanner.nextLine();
		boolean endOfMail = false;
		do {
			logger.debug("Start Boundary: " + boundary);
			parseBoundary(scanner, boundary, boundaryLine);
			String line = scanner.nextLine();
			endOfMail = line.equals(".");
			if (!endOfMail && !line.startsWith("--" + boundary)) {
				throw new IllegalStateException("Unexpected line: " + line + ". Expected end of bondary.");				
			}
		} while (!endOfMail);
	}

	private void parseBoundary(Scanner scanner, String boundary, String lastLine) throws IOException {
		if (!lastLine.equals("--" + boundary)) {
			throw new IllegalStateException("Expected beggining of boundary");
		}
		Map<String, MimeHeader> headers = readBoundaryHeaders(scanner);
		MimeHeader contentType = headers.get("Content-Type");
		if (contentType != null) {
			String subBoundary = contentType.getExtraValue("boundary");
			if (subBoundary != null) {
				logger.info("Sub boundary " + subBoundary + " found. Recursively parsing.");
				parseBoundary(scanner, subBoundary, scanner.nextLine());
				return;
			}
		}
		StringBuilder text = new StringBuilder();
		boolean endOfBoundary = false;
		String line;
		do {
			line = scanner.nextLine();
			endOfBoundary = line.startsWith("--" + boundary);
			if (!endOfBoundary) {
				text.append(line + "\r\n");
			}
		} while (!endOfBoundary);
		// TODO: hacer todas las transformacione pertinentes a text y header con el objeto MailTransformer
		if (line.equals("--" + boundary)) {
			parseBoundary(scanner, boundary, line);
			return;
		} else if (line.equals("--" + boundary + "--")) {
			return;
		} else {
			throw new IllegalStateException("Unexpected line: " + line);
		}
	}

	private Map<String, MimeHeader> readBoundaryHeaders(Scanner scanner) {
		Map<String, MimeHeader> headers = new HashMap<String, MimeHeader>();
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			// according to rfc, an empty line marks the of sub-boundary headers
			if (line.equals("")) {
				break;
			}
			MimeHeader header = new MimeHeader(line);
			headers.put(header.getKey(), header);
			logger.debug("Parsed boundary header => " + header);
		}
		return headers;
	}
}
