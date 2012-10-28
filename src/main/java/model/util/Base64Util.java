package model.util;

import org.apache.commons.codec.binary.Base64;

public class Base64Util {

	public static String decode(String text) {
		return new String(new Base64().decode(text));
	}

}
