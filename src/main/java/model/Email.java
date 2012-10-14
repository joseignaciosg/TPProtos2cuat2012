package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.joda.time.LocalDate;

import parser.MailHeader;
import service.command.impl.mail.DeleCommand;
import util.StringUtil;

public class Email {
	
	protected static final Logger logger = Logger.getLogger(Email.class);

	private Double size;
	private LocalDate date;
	private String sender;
	private MailHeader header;
	private List<String> attachmentsExtension;
	
	public Email(File mailFile) {
		attachmentsExtension = new ArrayList<String>();
		header = new MailHeader(mailFile);
	}
	
	public Double getSize() {
		return size;
	}
	public LocalDate getDate() {
		return date;
	}
	public String getSender() {
		return sender;
	}
	public MailHeader getHeader() {
		return header;
	}
	public boolean hasAttachments() {
		return attachmentsExtension.size() > 0;
	}
	public List<String> getAttachmentsExtension() {
		return attachmentsExtension;
	}
	
	public boolean hasAttachmentWithExtension(String str){
		return attachmentsExtension.contains(str.trim());
	}
	
	
	public String getHeader(String str){
		try {
			return header.getHeader(str);
		} catch (IOException e) {
			logger.error("IO error while trying to retrive mail header " + str);
			throw new IllegalStateException();
		}
	}
	
	

}
