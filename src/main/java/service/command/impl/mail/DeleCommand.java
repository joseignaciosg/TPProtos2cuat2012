package service.command.impl.mail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import model.Email;

import org.apache.log4j.Logger;

import service.AbstractSockectService;
import service.MailSocketService;
import service.command.ServiceCommand;
import validator.DelContentTypeValidator;
import validator.DelHeaderPatternValidator;
import validator.DelMaxAgeValidator;
import validator.DelSenderValidator;
import validator.DelSizeValidator;
import validator.DelStructureValidator;
import validator.EmailValidator;

public class DeleCommand extends ServiceCommand {

	protected static final Logger logger = Logger.getLogger(DeleCommand.class);

	List<EmailValidator> validators;

	public DeleCommand(AbstractSockectService owner) {
		super(owner);
		validators = new ArrayList<EmailValidator>();
		validators.add(new DelContentTypeValidator());
		validators.add(new DelHeaderPatternValidator());
		validators.add(new DelMaxAgeValidator());
		validators.add(new DelSenderValidator());
		validators.add(new DelSizeValidator());
		validators.add(new DelStructureValidator());
	}

	@Override
	public void execute(String[] params) {
		String[] retrParams = { "RETR", params[0] };
		owner.getStateMachine().exec(retrParams);
		Email email = (Email) getBundle().get("DELE_" + params[0]);
		for (EmailValidator v : validators) {
			if (!v.validate(email)) {
				// error
				getBundle().remove(params[0]);

				return;
			}
		}
		getBundle().remove(params[0]);
		MailSocketService mailService = (MailSocketService) owner;
		mailService.echoLineToOriginServer(getOriginalLine());
		try {
			mailService.echoLine(mailService.readFromOriginServer().readLine());
		} catch (IOException e) {
			logger.error("Error while echoing line to client");
			throw new IllegalStateException();
		}
	}

}
