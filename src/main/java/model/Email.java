package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.LocalDate;

import parser.MailHeader;

public class Email {
	
	protected static final Logger logger = Logger.getLogger(Email.class);

	private long sizeInBytes;
	private LocalDate date;
	private String sender;
	private MailHeader header;
	private List<String> attachmentsExtension;
	
	public Email(File mailFile) {
		attachmentsExtension = new ArrayList<String>();
		header = new MailHeader(mailFile);
	}
	
	public long getSize() {
		return sizeInBytes;
	}
	public LocalDate getDate() {
		return date;
	}
	public String getSender() {
		return sender;
	}
	public MailHeader getHeaders() {
		return header;
	}
	public boolean hasAttachments() {
		return attachmentsExtension.size() > 0;
	}
	public List<String> getAttachmentsExtension() {
		return attachmentsExtension;
	}
	
	public boolean hasAttachmentWithExtension(String str){
		if(hasAttachments()){
			return attachmentsExtension.contains(str);
		}
		
		return false;
	}
	
	public String getHeader(String str){
		try {
			return header.getHeader(str);
		} catch (IOException e) {
			logger.error("IO error while trying to retrive mail header " + str);
			throw new IllegalStateException();
		}
	}
	
	public String getHumanReadableSize() {
		boolean si = true;
	    int unit = si ? 1000 : 1024;
	    if (sizeInBytes < unit) return sizeInBytes + " B";
	    int exp = (int) (Math.log(sizeInBytes) / Math.log(unit));
	    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
	    return String.format("%.1f %sB", sizeInBytes / Math.pow(unit, exp), pre);
	}

	public boolean hasHeaderValue(String key, String value) {
		return false;
	}

}
