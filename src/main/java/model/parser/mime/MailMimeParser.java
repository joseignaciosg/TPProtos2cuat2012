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

	public Mail parse(File source, long sizeInBytes, MailTransformer transformer) throws IOException {
		Mail mail = new Mail(source);
		mail.setSizeInBytes(sizeInBytes);
		Scanner sourceScanner = new Scanner(source);
		File destination = File.createTempFile("transformed_", source.getName());
		FileWriter destinationWriter = new FileWriter(destination);
		parseMainBoundary(new ParseParameters(mail, sourceScanner, destinationWriter, transformer));
		sourceScanner.close();
		destinationWriter.flush();
		destinationWriter.close();
		return mail;
	}

	private void parseMainBoundary(ParseParameters parseParams) throws IOException {
		headerParser.parse(parseParams.sourceScanner, parseParams.mail, parseParams.destinaionWriter, parseParams.transformer);
		String boundary = parseParams.mail.getBoundaryKey();
		boolean endOfMail = false;
		String line = parseParams.sourceScanner.nextLine();
		do {
			logger.debug("Start Boundary: " + boundary);
			parseBoundary(parseParams, boundary, line);
			line = parseParams.sourceScanner.nextLine();
			endOfMail = line.equals(".");
			if (!endOfMail && !line.startsWith("--" + boundary)) {
				throw new IllegalStateException("Unexpected line: " + line + ". Expected end of bondary.");				
			}
		} while (!endOfMail);
		parseParams.destinaionWriter.append(line + "\r\n");
	}

	private void parseBoundary(ParseParameters parseParams, String boundaryKey, String lastLine) throws IOException {
		if (!lastLine.equals("--" + boundaryKey)) {
			throw new IllegalStateException("Expected beggining of boundary");
		}
		parseParams.destinaionWriter.append("--" + boundaryKey + "\r\n");
		Scanner sourceScanner = parseParams.sourceScanner;
		Map<String, MimeHeader> headers = readBoundaryHeaders(parseParams);
		MimeHeader contentType = headers.get("Content-Type");
		String subBoundary = contentType.getExtraValue("boundary");
		if (subBoundary != null) {
			logger.info("Sub boundary " + subBoundary + " found. Recursively parsing.");
			parseBoundary(parseParams, subBoundary, sourceScanner.nextLine());
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
		// XXX: Append transformed body content
		parseParams.destinaionWriter.append(parseParams.transformer.transform(text, headers));
		if (line.equals("--" + boundaryKey)) {
			parseBoundary(parseParams, boundaryKey, line);
			return;
		} else if (line.equals("--" + boundaryKey + "--")) {
			parseParams.destinaionWriter.append(line + "\r\n"); // Dont forget end of boundary!
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
			// according to rfc, an empty line marks the of sub-boundary headers
			if (line.equals("")) {
				parseParams.destinaionWriter.append(line + "\r\n");
				break;
			}
			MimeHeader header = new MimeHeader(line);
			headers.put(header.getKey(), header);
			parseParams.destinaionWriter.append(header.getOriginalLine() + "\r\n");
			logger.debug("Parsed boundary header => " + header);
		}
		return headers;
	}
	
	private static class ParseParameters {
		Mail mail;
		Scanner sourceScanner;
		FileWriter destinaionWriter;
		MailTransformer transformer;
		
		public ParseParameters(Mail mail, Scanner sourceScanner, FileWriter destinaionWriter, MailTransformer trasformer) {
			this.mail = mail;
			this.sourceScanner = sourceScanner;
			this.destinaionWriter = destinaionWriter;
			this.transformer = trasformer;
		}
	}
}
