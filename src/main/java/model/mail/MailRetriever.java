package model.mail;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class MailRetriever {
	
	public void retrieve(BufferedReader inputBuffer, DataOutputStream outStream) throws IOException {
		String line;
		do {
			line = inputBuffer.readLine();
			outStream.writeBytes(line + "\r\n");
		} while (!line.equals("."));
	}
	
	public File retrieve(String name, BufferedReader inputBuffer) throws IOException {
		File mailTmpFile = File.createTempFile("mail" + name, ".mail");
		FileWriter mailFileWriter = new FileWriter(mailTmpFile);
		String line;
		do {
			line = inputBuffer.readLine();
			mailFileWriter.append(line + "\r\n");
		} while (!line.equals("."));
		mailFileWriter.close();
		return mailTmpFile;
	}
	
}
