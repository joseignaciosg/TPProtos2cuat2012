package service.command.impl.mail;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.User;
import model.mail.Mail;
import model.validator.MailValidator;
import model.validator.MailValidationException;
import model.validator.emailvalidator.DelContentTypeValidator;
import model.validator.emailvalidator.DelHeaderPatternValidator;
import model.validator.emailvalidator.DelMaxDateValidator;
import model.validator.emailvalidator.DelSenderValidator;
import model.validator.emailvalidator.DelSizeValidator;
import model.validator.emailvalidator.DelStructureValidator;

import org.apache.log4j.Logger;

import service.AbstractSockectService;
import service.MailSocketService;
import service.command.ServiceCommand;

public class DeleCommand extends ServiceCommand {

	protected static final Logger logger = Logger.getLogger(DeleCommand.class);

	private List<MailValidator> validators;

	public DeleCommand(AbstractSockectService owner) {
		super(owner);
		validators = new ArrayList<MailValidator>();
		validators.add(new DelContentTypeValidator());
		validators.add(new DelHeaderPatternValidator());
		validators.add(new DelMaxDateValidator());
		validators.add(new DelSenderValidator());
		validators.add(new DelSizeValidator());
		validators.add(new DelStructureValidator());
	}

	@Override
	public void execute(String[] params) throws Exception {
		MailSocketService mailService = (MailSocketService) owner;
		Mail email = getMail(params[0]);
		User current = (User) getBundle().get("AUTH_USER");
		for (MailValidator validator : validators) {
			try {
				validator.validate(current, email);
			} catch (MailValidationException e) {
				logger.info(e.getMessage());
				owner.echoLine("-ERR " + e.getMessage() + ".");
				return;
			}
		}
		mailService.echoLineToOriginServer(getOriginalLine());
		mailService.echoLine(mailService.readFromOriginServer().readLine());
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
