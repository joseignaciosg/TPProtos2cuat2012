package model.mail;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import model.User;
import service.command.impl.stats.StatsService;


public class MailRetriever {
	
	private static final StatsService statsService = StatsService.getInstace();
	
	public void retrieve(BufferedReader inputBuffer, DataOutputStream outStream, User user) throws IOException {
		String line;
		do {
			line = inputBuffer.readLine();
			String out = line + "\r\n";
			outStream.writeBytes(out);
			statsService.incrementTransferedBytes(out.length(), user.getMail());
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
