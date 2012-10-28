package model.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
	
	public static File createFileWithContents(String text) throws IOException {
		File file = File.createTempFile("decode_", ".tmp");
		FileWriter writer = new FileWriter(file);
		writer.append(text);
		writer.flush();
		writer.close();
		return file;
	}
}
