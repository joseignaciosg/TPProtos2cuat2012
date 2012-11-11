package model.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;


public class IOUtil {

private static final IOUtil instance = new IOUtil();
	
	@SuppressWarnings("resource")
	public static Scanner createScanner(String resourcePath) {
		InputStream is = getStream(resourcePath);
		return (is == null) ? null : new Scanner(is);
	}
	
	public static InputStream getStream(String resourcePath) {
		return instance.getClass().getClassLoader().getResourceAsStream(resourcePath);
	}
	
	public static URL getUrl(String resorucePath) throws URISyntaxException {
		return instance.getClass().getClassLoader().getResource(resorucePath);
	}
	
	public static String getRoot() throws URISyntaxException {
		return instance.getClass().getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
	}
	
	public static File createFileWithContents(String text) throws IOException {
		File file = File.createTempFile("decode_", ".tmp");
		FileWriter writer = new FileWriter(file);
		writer.append(text);
		writer.flush();
		writer.close();
		return file;
	}
	
	public static void redirectOutputStream(InputStream inputStream, File out) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		FileWriter writer = new FileWriter(out);
		String line;
		while ((line = reader.readLine()) != null) {
			writer.write(line + "\n");
		}
		writer.flush();
		writer.close();
		reader.close();
	}
	
}
