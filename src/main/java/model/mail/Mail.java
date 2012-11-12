package model.mail;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import model.parser.mime.MimeHeader;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Mail {

	private MimeHeaderCollection headers;
	private Set<String> attachmentsExtension;
	private File contents;

	public Mail(File contents) {
		headers = new MimeHeaderCollection();
		attachmentsExtension = new HashSet<String>();
		setContents(contents);
	}

	public long getSizeInBytes() {
		return contents == null ? 0 : contents.length();
	}
	
	
	public void setContents(File contents) {
		this.contents = contents;
	}
	
	public void addAttachmentsExtension(String extension) {
		attachmentsExtension.add(extension.toLowerCase());
	}

	public Set<String> getAttachmentsExtension() {
		return attachmentsExtension;
	}

	public boolean hasAttachments() {
		return attachmentsExtension.size() > 0;
	}

	public boolean hasAttachmentWithExtension(String extension) {
		return attachmentsExtension.contains(extension.toLowerCase());
	}

	public void addHeader(MimeHeader header) {
		headers.add(header);
	}

	public MimeHeader getHeader(String name) {
		return headers.get(name);
	}

	public String getBoundaryKey() {
		MimeHeader header = headers.get("Content-Type");
		if (header == null) {
			return null;
		}
		return header.getExtraValue("boundary");
	}

	public LocalDate getDate() {
		String headerDate = headers.get("Date").getValue();
		DateTimeFormatter formatter =
			    DateTimeFormat.forPattern("E, dd MMM yyyy kk:mm:ss Z").withOffsetParsed();
		LocalDate date;
		try{
			date = formatter.parseDateTime(headerDate).toLocalDate();
		}catch(Exception e){
			throw new IllegalArgumentException("Could not parse month:"+ headerDate);
		}
		
		return date;
	}

	/*
	 * RFC 2822 specifies:
	 * 
	 *  from            =       "From:" mailbox-list CRLF
	 *  sender          =       "Sender:" mailbox CRLF
	 *  
	 *  The from field consists of the field name "From" and a
	 *  comma-separated list of one or more mailbox specifications
	 *  
	 *  the field name "Sender" and a single mailbox specification
	 *  
	 * */
	public String getSender() {
		String sender = null;
		boolean lookForSender = false;
		String[] headerFromArray = headers.get("from").getValue().split(",");
		if(headerFromArray.length == 1){
			sender = headerFromArray[0];
			if (sender==null){
				lookForSender = true;
			}
		}else{
			lookForSender = true;
		}
			
		if(lookForSender){
			sender = headers.get("sender").getValue();
		}
		
		String[] aux;
		if(sender.contains("<")){
			aux = sender.split("<");
			sender = aux[1];
			aux = sender.split(">");
			sender = aux[0];
		}else if (sender.contains(" ")){
			aux = sender.split(" ");
			sender = aux[1];
		}
		return sender;
	}

	public boolean containsHeader(String key, String value) {
		return headers.contains(key, value);
	}

	public boolean headerMatches(String key, String match) {
		return headers.headerMatches(key, match);
	}
	
	public File getContents() {
		return contents;
	}

	public boolean isMultiPart() {
	    return getBoundaryKey() != null;
	}

	public MimeHeaderCollection getHeaders() {
		return headers;
	}

	public void delete() {
		contents.delete();
	}
}
