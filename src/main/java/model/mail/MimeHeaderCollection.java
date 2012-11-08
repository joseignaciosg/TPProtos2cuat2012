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

	public boolean contains(String key, String value) {
		MimeHeader header = get(key);
		if (header == null) {
			return false;
		}
		if (value == null) {
			return header.getValue() == null;
		}
		return header.getValue().toLowerCase().equals(value.toLowerCase());
	}

	public boolean headerMatches(String key, String match) {
		MimeHeader header = get(key);
		if (header == null) {
			return false;
		}
		return header.getValue().toLowerCase().contains(match.toLowerCase());
	}
}
