package model.parser.mime;

import java.util.LinkedList;
import java.util.List;

import model.util.CollectionUtil;
import model.util.StringUtil;


public class MimeHeader {

	private String key, value;
	private List<String> extraValues;
	
	public MimeHeader(String s) {
		validateLine(s);
		int splitIndex = s.indexOf(":");
		key = s.substring(0, splitIndex);
		int endIndex = s.indexOf(";");
		endIndex = (endIndex == -1) ? s.length() : endIndex;
		value = s.substring(splitIndex + 2, endIndex);
		extraValues = new LinkedList<String>();
		if (endIndex != s.length()) {			
			String exavalueLine = s.substring(endIndex + 1);
			String[] parts = exavalueLine.split(";");
			for (String part : parts) {
				extraValues.add(part);
			}
		}
	}
	
	private void validateLine(String line) {
		String error = null;
		if (StringUtil.empty(line)) {
			error = "Line can not be empty";
		}
		if (line.startsWith(".") || line.startsWith(" ")) {
			error = "Continuation from previous header";
		}
		if (error != null) {
			throw new IllegalArgumentException(error);
		}
	}
	
	public String getKey() {
		return key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public String getValue() {
		return StringUtil.unquote(value);
	}
	
	public void setValue(String value) {
		this.value = value;
	}
	
	public String getExtraValue(String name) {
		for (String keyValuePair : extraValues) {
			String cleaned = keyValuePair.replace("\r", "").replace("\n", "");
			int splitIndex = cleaned.indexOf("=");
			if (splitIndex == -1) {
				return null;
			}
			String key = cleaned.substring(0, splitIndex).trim();
			String extraValue = cleaned.substring(splitIndex + 1).trim();
			if (name.equalsIgnoreCase(key)) {
				return StringUtil.unquote(extraValue);
			}
		}
		return null;
	}
	
	@Override
	public int hashCode() {
		return key.hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		return key.equals(((MimeHeader) obj).getKey());
	}
	
	@Override
	public String toString() {
		if (!extraValues.isEmpty()) {
			StringBuilder sb = new StringBuilder();
			sb.append(key + ": " + value + ";");
			sb.append(CollectionUtil.join(extraValues, ";"));
			return sb.toString();
		}
		return key + ": " + value;
	}

}
