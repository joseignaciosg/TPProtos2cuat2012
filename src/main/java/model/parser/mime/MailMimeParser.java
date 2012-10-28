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

	public Mail parse(File source, MailTransformer transformer) throws IOException {
		File destination = File.createTempFile("transformed_", source.getName());
		Mail mail = new Mail(destination);
		Scanner sourceScanner = new Scanner(source);
		FileWriter destinationWriter = new FileWriter(destination);
		parseMainBoundary(new ParseParameters(mail, sourceScanner, destinationWriter, transformer));
		sourceScanner.close();
		destinationWriter.flush();
		destinationWriter.close();
		return mail;
	}

	private void parseMainBoundary(ParseParameters parseParams) throws IOException {
		headerParser.parse(parseParams);
		String boundary = parseParams.mail.getBoundaryKey();
		parseParams.mail.setMultipartMail(boundary != null);
		boolean endOfMail = false;
		String line = parseParams.sourceScanner.nextLine();
		if (parseParams.mail.isMultipartMail()) {
			do {
				logger.debug("Start Boundary: " + boundary);
				parsePart(parseParams, boundary, line);
				line = parseParams.sourceScanner.nextLine();
				endOfMail = line.equals(".");
				if (!endOfMail && !line.startsWith("--" + boundary)) {
					throw new IllegalStateException("Unexpected line: " + line + ". Expected end of bondary.");
				}
			} while (!endOfMail);
		} else {
			Map<String, MimeHeader> headers = new HashMap<String, MimeHeader>();
			do {
				MimeHeader header = new MimeHeader("Content-Type: text/plain");
				headers.put(header.getKey(), header);
				StringBuilder transLine = parseParams.transformer.transformPart(headers, new StringBuilder(line));
				parseParams.destinationWriter.append(transLine.toString() + "\r\n");
				line = parseParams.sourceScanner.nextLine();
				endOfMail = line.equals(".");
			} while (!endOfMail);
		}
		parseParams.destinationWriter.append(line + "\r\n");
	}

	private void parsePart(ParseParameters parseParams, String boundaryKey, String lastLine) throws IOException {
		if (!lastLine.equals("--" + boundaryKey)) {
			throw new IllegalStateException("Expected beggining of boundary");
		}
		parseParams.destinationWriter.append("--" + boundaryKey + "\r\n");
		Scanner sourceScanner = parseParams.sourceScanner;
		Map<String, MimeHeader> headers = readBoundaryHeaders(parseParams);
		MimeHeader contentType = headers.get("Content-Type");
		String subBoundary = contentType.getExtraValue("boundary");
		if (subBoundary != null) {
			logger.info("Sub Part " + subBoundary + " found. Recursively parsing.");
			parsePart(parseParams, subBoundary, sourceScanner.nextLine());
			return;
		}
		StringBuilder text = new StringBuilder();
		boolean endOfBoundary = false;
		String line;
		do {
			line = sourceScanner.nextLine();
			endOfBoundary = line.startsWith("--" + boundaryKey);
			if (!endOfBoundary) {
				text.append(line + "\r\n");
			}
		} while (!endOfBoundary);
		parseParams.destinationWriter.append(parseParams.transformer.transformPart(headers, text));
		if (line.equals("--" + boundaryKey)) {
			parsePart(parseParams, boundaryKey, line);
			return;
		} else if (line.equals("--" + boundaryKey + "--")) {
			parseParams.destinationWriter.append(line + "\r\n");
			return;
		} else {
			throw new IllegalStateException("Unexpected line: " + line);
		}
	}

	private Map<String, MimeHeader> readBoundaryHeaders(ParseParameters parseParams) throws IOException {
		Map<String, MimeHeader> headers = new HashMap<String, MimeHeader>();
		Scanner sourceScanner = parseParams.sourceScanner;
		while (sourceScanner.hasNextLine()) {
			String line = sourceScanner.nextLine();
			// Empty line marks the end of sub-boundary headers
			if (line.equals("")) {
				parseParams.destinationWriter.append(line + "\r\n");
				break;
			}
			MimeHeader header = new MimeHeader(line);
			headers.put(header.getKey(), header);
			parseParams.destinationWriter.append(header.toString() + "\r\n");
			logger.debug("Parsed Part header => " + header);
		}
		return headers;
	}

}
