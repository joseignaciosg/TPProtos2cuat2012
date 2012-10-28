package service.command.impl.mail;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import model.User;
import model.mail.Mail;

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
		MailSocketService mailSocketService = (MailSocketService) owner;
		mailSocketService.echoLineToOriginServer(getOriginalLine());
		BufferedReader mailInStream = mailSocketService.readFromOriginServer();
		String firstLine = mailInStream.readLine();
		if (!firstLine.toUpperCase().startsWith("+OK")) {
			return;
		}
		File mailContent = mailSocketService.getMailRetriever().retrieve(params[0], mailInStream);
		Mail mail = mailSocketService.getMailMimeParser().parse(mailContent, mailSocketService.getMailTranformer());
		// Ignore first line because transformed mail may differ in size from original mail
		mailSocketService.echoLine("+OK " + mail.getSizeInBytes() + " octets");
		echoMailToClient(mail);
		mailContent.delete();
		((User) getBundle().get("user")).setMail(params[0], mail);
	}

	private void echoMailToClient(Mail mail) throws IOException {
		String userMail = ((User) getBundle().get("user")).getMail();
		Scanner s = new Scanner(mail.getContents());
		logger.info("echoing mail to client...");
		while (s.hasNextLine()) {
			owner.echoLine(s.nextLine());
		}
		statsService.incrementNumberOfReadMail(userMail);
		statsService.incrementTransferedBytes(mail.getSizeInBytes(), userMail);
		s.close();
	}
}
