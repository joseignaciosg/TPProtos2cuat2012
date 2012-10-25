package model.parser.mime;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
		File transformedFile = File.createTempFile(mimeFile.getName()+"trans", "trans");
		mail.setSizeInBytes(sizeInBytes);
		Scanner scanner = new Scanner(mimeFile);
		FileWriter transWriter = new FileWriter(transformedFile);
		headerParser.parse(scanner, mail);
		parsePart(scanner, mail.getBoundaryKey(), mail, transformer);
		scanner.close();
		return mail;
	}

	private void parsePart(Scanner scanner, String boundary, Mail mail,MailTransformer transformer ) throws IOException{
		boolean cont = false;
	    	StringBuilder content = new StringBuilder();
	    	String line = null;
	    	List<MimeHeader> headers;
	    	while (scanner.hasNextLine()) {
	    	    	if(cont){
	    	    	    	line  = scanner.nextLine();
	    	    	    	cont = false;
	    	    	}
			if ( line.equals("--" + boundary)) {
			    	// start of a segment
				logger.info("PART START : " + boundary);
				//parse header
				headers = parsePartHeader(scanner, mail,transformer);
				//parse content
				while (scanner.hasNextLine()){
				    String contLine = scanner.nextLine();
				    if(contLine.equals("--" + boundary)){
					cont = true;
					break;
				    }else if(line.equals("--" + boundary + "--")){
					return;
				    }	
				    content.append(contLine);
				}
				content = transformer.transform(content, headers);
				//TODO write to auxiliar File mail
			} else if (line.equals("--" + boundary + "--")) {
				logger.info("PART END : " + boundary);
				// end of mail file
				return;
			}
		}
	}



	private List<MimeHeader> parsePartHeader(Scanner scanner, Mail mail,MailTransformer transformer ) throws IOException {
		MimeHeader contentType = null;
		 List<MimeHeader> ret = new ArrayList<MimeHeader>();
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			// according to rfc, an empty line marks the of sub-boundary headers 
			if (line.equals("")) {
				break;
			}
			MimeHeader header = new MimeHeader(line);
			mail.addAttachmentsExtension(header.getValue());
			if (header.getKey().equals("Content-Type")) {
				contentType = header;
				ret.add(contentType);
				
			}
		}
		if (contentType != null) {
			String subBoundary = contentType.getExtraValue("boundary");
			if (subBoundary != null) {				
				logger.info("Sub boundary " + subBoundary + " found. Recursively parsing.");
				parsePart(scanner, subBoundary, mail,transformer);
			}
		}
		return ret;
	}
}
