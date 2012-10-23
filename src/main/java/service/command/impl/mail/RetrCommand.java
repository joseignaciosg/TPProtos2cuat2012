package service.command.impl.mail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;

import model.mail.Mail;
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
	public void execute(String[] params) throws Exception {
		// TODO: esta clase tiene que usar el MailRetriever del MailService!
		boolean showToClient = true;
		if (params.length == 3) {
			showToClient = Boolean.valueOf(params[3]);
		}
		File mailTmpFile = File.createTempFile("mail" + params[0], ".mail");
		FileWriter mailFileWriter = new FileWriter(mailTmpFile);
		MailSocketService mailService = (MailSocketService) owner;
		mailService.echoLineToOriginServer(getOriginalLine());
		BufferedReader responseBuffer = mailService.readFromOriginServer();
		String firstLine = null;
		String line;
		do {
			line = responseBuffer.readLine();
			if (firstLine == null) {
				firstLine = line;
			}
			mailFileWriter.append(line);
			if (showToClient) {
				mailService.echoLine(line);
			}
		} while (!line.equals("."));
		mailFileWriter.close();
		MailMimeParser parser = new MailMimeParser();
		int sizeInBytes = Integer.valueOf(firstLine.split(" ")[1]);
		Mail mail = parser.parse(mailTmpFile, sizeInBytes);
		if (!showToClient) {
			getBundle().put("DELE_" + params[0], mail);
		}
	}
}
