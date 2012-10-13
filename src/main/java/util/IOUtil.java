package util;

import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class IOUtil {

	public static String fullPath(String resourcePath) {
		URL url = getResource(resourcePath);
		return url == null ? null : url.getPath();
	}
	
	@SuppressWarnings("resource")
	public static Scanner createScanner(String resourcePath) {
		InputStream is = getStream(resourcePath);
		return (is == null) ? null : new Scanner(is);
	}
	
	public static InputStream getStream(String resourcePath) {
		return IOUtil.class.getClassLoader().getResourceAsStream(resourcePath);
	}
	
	public static URL getResource(String resourcePath) {
		return IOUtil.class.getClassLoader().getResource(resourcePath);
	}
}
