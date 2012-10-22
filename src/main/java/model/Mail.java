package model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import model.parser.mime.MimeHeader;

import org.joda.time.LocalDate;

public class Mail {

	private long sizeInBytes;
	private Map<String, MimeHeader> headers;
	private List<String> attachmentsExtension;

	public Mail() {
		headers = new HashMap<String, MimeHeader>();
		attachmentsExtension = new LinkedList<String>();
	}

	public long getSizeInBytes() {
		return sizeInBytes;
	}

	public void setSizeInBytes(long sizeInBytes) {
		this.sizeInBytes = sizeInBytes;
	}

	public void addAttachmentsExtension(String extension) {
		attachmentsExtension.add(extension);
	}

	public List<String> getAttachmentsExtension() {
		return attachmentsExtension;
	}

	public boolean hasAttachments() {
		return attachmentsExtension.size() > 0;
	}
	
	public boolean hasAttachmentWithExtension(String extension) {
		return attachmentsExtension.contains(extension);
	}
	
	public void addHeaders(MimeHeader header) {
		headers.put(header.getKey(), header);
	}
	
	public MimeHeader getHeader(String name) {
		return headers.get("name");
	}
	
	public String getBoundaryKey() {
		MimeHeader header = headers.get("Content-Type");
		return header == null ? null : header.getExtraValue("boundary");
	}
	
	public LocalDate getDate() {
		return new LocalDate(headers.get("Delivery-date").getValue());
	}

	public String getSender() {
		return headers.get("Return-path").getValue();
	}

	public boolean containsHeader(String key, String value) {
		MimeHeader header = headers.get(key);
		if (header == null) {
			return false;
		}
		return header.getValue().equals(value);
	}

}
