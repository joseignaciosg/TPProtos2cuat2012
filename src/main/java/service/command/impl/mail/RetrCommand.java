package service.command.impl.mail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import model.Mail;
import model.parser.mime.MailMimeParser;

import org.apache.log4j.Logger;


import service.AbstractSockectService;
import service.MailSocketService;
import service.command.ServiceCommand;

public class RetrCommand extends ServiceCommand {
	
	protected static final Logger logger = Logger.getLogger(DeleCommand.class);

	public RetrCommand(AbstractSockectService owner) {
		super(owner);
	}

	@Override
	public void execute(String[] params) {
		boolean showToClient = true;
		if (params.length == 3) {
			showToClient = Boolean.valueOf(params[3]);
		}
		File mailTmpFile;
		FileWriter mailFileWriter;
		try {
			mailTmpFile = File.createTempFile("mail" + params[0], ".mail");
			mailFileWriter = new FileWriter(mailTmpFile);
		} catch (IOException e) {
			logger.error("Error while trying to create the temporary mail file");
			throw new IllegalStateException();
		}
		MailSocketService mailService = (MailSocketService) owner;
		mailService.echoLineToOriginServer(getOriginalLine());
		BufferedReader responseBuffer = mailService.readFromOriginServer();
		String firstLine = null;
		String line;
		do {
			line = readLine(responseBuffer);
			if (firstLine == null) {
				firstLine = line;
			}
			try {
				mailFileWriter.append(line);
			} catch (IOException e) {
				try {
					mailFileWriter.close();
				} catch (IOException e1) {
				}
				throw new IllegalStateException("Error writing line - " + e.getMessage());
			}
			if (showToClient) {
				mailService.echoLine(line);
			}
		} while (!line.equals("."));
		try {
			mailFileWriter.close();
		} catch (IOException e) {
			throw new IllegalStateException("Error while closing temporary mail file - " + e.getMessage());
		}		
		MailMimeParser parser = new MailMimeParser();
		int sizeInBytes = Integer.valueOf(firstLine.split(" ")[1]);
		Mail mail;
		try {
			mail = parser.parse(mailTmpFile, sizeInBytes);
		} catch (IOException e1) {
			throw new IllegalStateException("Could not parse mail: " + mailTmpFile.getPath());
		}
		if (!showToClient) {
			getBundle().put("DELE_" + params[0], mail);
		}
	}

	public String readLine(BufferedReader responseBuffer) {
		try {
			return responseBuffer.readLine();
		} catch (IOException e) {
			throw new IllegalStateException("Error reading line - " + e.getMessage());
		}
	}
}
