package model.util;

public class StringUtil {

	public static boolean empty(String s) {
		return s == null || s.isEmpty();
	}

	public static String unquote(String s) {
		if (s == null) {
			return null;
		}
		if (s.startsWith("\"")) {
			s = s.substring(1, s.length() - 1);
		}
		return s;
	}
}
