package model.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
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

	public static File createFileWithContents(String text) throws IOException {
		File file = File.createTempFile("decode_", ".tmp");
		FileWriter writer = new FileWriter(file);
		writer.append(text);
		writer.flush();
		writer.close();
		return file;
	}

	public static void redirectOutputStream(InputStream inputStream, File out) throws IOException {
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(out));
		long length = inputStream.available();
		byte[] bytes = new byte[(int) length];
		// Read in the bytes
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length && (numRead = inputStream.read(bytes, offset, bytes.length - offset)) >= 0) {
			bos.write(bytes);
			offset += numRead;
		}
		bos.flush();
		bos.close();
	}

}
