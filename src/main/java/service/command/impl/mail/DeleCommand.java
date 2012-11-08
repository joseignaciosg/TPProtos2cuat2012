package service.command.impl.mail;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

import model.User;
import model.mail.Mail;
import model.util.CollectionUtil;
import model.validator.MailDeleteValidator;
import model.validator.MailValidationException;

import org.apache.log4j.Logger;

import service.AbstractSockectService;
import service.MailSocketService;
import service.command.ServiceCommand;

public class DeleCommand extends ServiceCommand {

	protected static final Logger logger = Logger.getLogger(DeleCommand.class);

	

	public DeleCommand(AbstractSockectService owner) {
		super(owner);
	}

	@Override
	public void execute(String[] params) throws Exception {
		MailSocketService mailService = (MailSocketService) owner;
		if (CollectionUtil.empty(params)) {
			owner.echoLine("-ERR missing msg argument.");
			return;
		}
		Mail email = getMail(params[0]);
		User current = (User) getBundle().get("user");
		MailDeleteValidator validator = mailService.getMailDeletionValidator();
		if (validator.hasRestrictions(current)) {
			logger.info(current.getMail() + " has restrictions for mail deleting.");
			try {
				validator.validate(current, email);
			} catch (MailValidationException e) {
				logger.info(e.getMessage());
				owner.echoLine("-ERR " + e.getMessage());
				return;
			}
		}
		mailService.echoLineToOriginServer(getOriginalLine());
		mailService.echoLine(mailService.readFromOriginServer().readLine());
		statsService.incrementNumberOfDeletedMail(current.getMail());
	}
	
	private Mail getMail(String mailName) throws IOException {
		MailSocketService mailService = (MailSocketService) owner;
		mailService.echoLineToOriginServer("RETR " + mailName);
		BufferedReader mailInputReader = mailService.readFromOriginServer();
		String statusLine = mailInputReader.readLine();
		if (!statusLine.toUpperCase().startsWith("+OK")) {
			throw new IllegalStateException("Could not retrieve mail " + mailName);
		}
		File source = mailService.getMailRetriever().retrieve(mailName, mailInputReader);
		return mailService.getMailMimeParser().parse(source, mailService.getMailTranformer());
	}
}
