package util;

import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class IOUtil {

	public static Scanner createScanner(String resourcePath) {
		return new Scanner(getStream(resourcePath));
	}
	
	public static InputStream getStream(String resourcePath) {
		return IOUtil.class.getClassLoader().getResourceAsStream(resourcePath);
	}
	
	public static URL getResource(String resourcePath) {
		return IOUtil.class.getClassLoader().getResource(resourcePath);
	}
}
