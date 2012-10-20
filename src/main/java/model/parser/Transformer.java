package model.parser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

import model.parser.mime.MailMimeParser;
import model.parser.mime.MimeHeaderParser;


public abstract class Transformer {

	MimeHeaderParser header;

	protected File mail;
	protected File transformedMail;
	protected String boundaryline;

	public Transformer() {
	}

	public File apply(File mailFile) throws IOException {
		MailMimeParser mailParser = new MailMimeParser();
		this.mail = mailFile;
		/*
		mail = mailParser.parse(mailFile);
		transformedMail = File.createTempFile("transformedmail", "proxy");
		FileWriter out = new FileWriter(transformedMail, true);
		Scanner scanner = new Scanner(mail);
		final String boundary = header.getBoundaryKey();
		boolean inheader = true;
		while (scanner.hasNextLine()) {
			if (inheader) {
				final String line = scanner.nextLine();
				if (Pattern.matches(".*--" + boundary + ".*", line)) {
					boundaryline = line;
					inheader = false;
					out.close();
				} else {
					out.write(line + "\r\n");
				}
			} else {
				String partheaders = getPartHeaders(scanner);
				File part = readPart(scanner, boundary);
				writePart(transform(part, partheaders));
			}
		}
		out = new FileWriter(transformedMail, true);
		out.write(boundaryline + "--");
		out.close();
		scanner.close();
		*/
		return transformedMail;

	}

	private File readPart(Scanner scanner, final String boundary)
			throws IOException {
		File part = File.createTempFile("part", "proxy");
		FileWriter out = new FileWriter(part, true);
		String line;
		// part payload
		do {
			line = scanner.nextLine();
			if (!Pattern.matches(".*--" + boundary + ".*", line)) {
				out.write(line + "\r\n");
			}
		} while (scanner.hasNextLine()
				&& !Pattern.matches(".*--" + boundary + ".*", line));
		out.close();
		return part;
	}

	private String getPartHeaders(Scanner scanner) throws IOException {
		String line;
		FileWriter outToMail = new FileWriter(transformedMail, true);
		outToMail.write(boundaryline + "\r\n");
		StringBuilder partHeaders = new StringBuilder();
		// part headers
		do {
			line = scanner.nextLine();
			outToMail.write(line + "\r\n");
			partHeaders.append(line + "\r\n");
		} while (scanner.hasNextLine() && !line.equals(""));
		outToMail.close();
		return partHeaders.toString();
	}

	private void writePart(File part) throws IOException {
		FileWriter out = new FileWriter(transformedMail, true);
		Scanner partScanner = new Scanner(part);
		while (partScanner.hasNextLine()) {
			String line = partScanner.nextLine();
			out.write(line + "\r\n");
		}
		out.close();
		partScanner.close();
	}

	public abstract File transform(File part, String partheaders)
			throws IOException;

}