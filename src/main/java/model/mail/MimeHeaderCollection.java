package model.mail;

import java.util.HashMap;
import java.util.Map;

import model.parser.mime.MimeHeader;

public class MimeHeaderCollection {

	private Map<String, MimeHeader> headers;
	
	public MimeHeaderCollection() {
		headers = new HashMap<String, MimeHeader>();
	}
	
	public void add(MimeHeader header) {
		headers.put(header.getKey().toLowerCase(), header);
	}
	
	public MimeHeader get(String key) {
		return headers.get(key.toLowerCase());
	}
}
