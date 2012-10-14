package model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.LocalDate;

import parser.MailHeader;

public class Email {
	
	protected static final Logger logger = Logger.getLogger(Email.class);

	private File mailFile;
	private MailHeader header;
	private long sizeInBytes;
	
	private List<String> attachmentsExtension;
	
	public Email(String okLine, File mailFile) {
		this.mailFile = mailFile;
		header = new MailHeader(mailFile);
		sizeInBytes = Integer.valueOf(okLine.split(" ")[1]);
		attachmentsExtension = new ArrayList<String>();
	}
	
	public long size() {
		return sizeInBytes;
	}
	
	public LocalDate getDate() {
		return new LocalDate(header.getHeader("Delivery-date"));
	}
	
	public String getSender() {
		return header.getHeader("Return-path");
	}
		
	public boolean hasAttachments() {
		return attachmentsExtension.size() > 0;
	}
	
	public List<String> getAttachmentsExtension() {
		return attachmentsExtension;
	}
	
	public boolean hasAttachmentWithExtension(String str){
		return attachmentsExtension.contains(str);
	}
	
	public String getHumanReadableSize() {
		boolean si = true;
	    int unit = si ? 1000 : 1024;
	    if (sizeInBytes < unit) { 
	    	return sizeInBytes + " B";
	    }
	    int exp = (int) (Math.log(sizeInBytes) / Math.log(unit));
	    String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
	    return String.format("%.1f %sB", sizeInBytes / Math.pow(unit, exp), pre);
	}

	public boolean headerMatches(String key, String value) {
		String headerValue = header.getHeader(key);
		if (headerValue == null) {
			return false;
		}
		return headerValue.equals(value);
	}
	
	public File getMailFile() {
		return mailFile;
	}

}
