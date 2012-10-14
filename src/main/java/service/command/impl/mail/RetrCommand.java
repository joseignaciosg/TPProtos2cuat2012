package service.command.impl.mail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.apache.log4j.Logger;

import model.Email;
import service.AbstractSockectService;
import service.MailSocketService;
import service.command.ServiceCommand;
import util.IOUtil;

public class RetrCommand extends ServiceCommand {
	protected static final Logger logger = Logger.getLogger(DeleCommand.class);

	public RetrCommand(AbstractSockectService owner) {
		super(owner);
	}

	@Override
	public void execute(String[] params) {
		boolean showToClient = true;
		if (params.length == 3) {
			showToClient = params[3].equals("false") ? false : true;
		}
		FileWriter mailFile;
		try {
			mailFile = new FileWriter(File.createTempFile("mail" + params[0],
					".mail"));
		} catch (IOException e) {
			logger.error("Error while trying to create the temporary mail file");
			throw new IllegalStateException();

		}

		MailSocketService mailService = (MailSocketService) owner;
		mailService.echoLineToOriginServer("RETR " + params[0]);

		BufferedReader responseBuffer = mailService.readFromOriginServer();
		String line;
		do {
			line = readLine(responseBuffer);
			try {
				mailFile.append(line);
			} catch (IOException e) {
				throw new IllegalStateException("Error writing line - "
						+ e.getMessage());
			}
			if (showToClient) {
				mailService.echoLine(line);
			}
		} while (!line.equals(".\r\n"));
		
		if (!showToClient) {
			getBundle().put("DELE_" + params[0],
					new Email(new File(IOUtil.fullPath("test_mail.txt"))));
		}
		
		try {
			mailFile.close();
		} catch (IOException e) {
			throw new IllegalStateException(
					"Error while closing temporary mail file - "
							+ e.getMessage());
		}
	}

	public String readLine(BufferedReader responseBuffer) {
		try {
			return responseBuffer.readLine();
		} catch (IOException e) {
			throw new IllegalStateException("Error reading line - "
					+ e.getMessage());
		}
	}
}
