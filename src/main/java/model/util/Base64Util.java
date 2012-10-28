package model.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.commons.codec.binary.Base64;

public class Base64Util {

	public static String decode(String text) {
		return new String(new Base64().decode(text));
	}
	
	public static File encodeUsingOS(File file) throws IOException, InterruptedException {
		File encodedContents = File.createTempFile("encoded", "jpg");
		FileInputStream fis = new FileInputStream(file);
		FileOutputStream fos = new FileOutputStream(encodedContents);
		int bufferSize = 2048;
		byte[] fileBytes = new byte[bufferSize];
		while (fis.read(fileBytes) != -1) {
			fos.write(new Base64().encode(fileBytes));
		}
		fos.flush();
		fos.close();
		fis.close();
		return encodedContents;
	}
	
	public static File decodeUsingOS(String text) throws IOException, InterruptedException {
		File file = IOUtil.createFileWithContents(text);
		byte[] decodedBytes = new Base64().decode(text.getBytes());
		FileOutputStream fos = new FileOutputStream(file);
		fos.write(decodedBytes);
		fos.flush();
		fos.close();
		return file;
	}

	
}
