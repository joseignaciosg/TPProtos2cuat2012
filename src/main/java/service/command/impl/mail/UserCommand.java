package service.command.impl.mail;

import java.io.IOException;

import model.User;
import model.validator.LoginValidationException;
import model.validator.loginvalidator.AccessCountValidator;
import model.validator.loginvalidator.TimeValidator;
import service.AbstractSockectService;
import service.MailSocketService;
import service.command.ServiceCommand;
import service.state.impl.mail.ParseMailState;

public class UserCommand extends ServiceCommand {

	public UserCommand(AbstractSockectService owner) {
		super(owner);
	}

	@Override
	public void execute(String[] params) throws Exception {
		MailSocketService mailServer = (MailSocketService) owner;
		User user = new User(params[0], null);
		if (!validateAccessToMailByTime(user) || !validateAccessToMailByCount(user)) {
			return;
		}
		mailServer.setOriginServer(user.getMailhost());
		String resp = echoToOriginServerAndReadLine(getOriginalLine());
		owner.echoLine(resp);
		if (!"+OK".equals(resp)) {
			return;
		}
		String passwordCmd = owner.read().readLine();
		resp = echoToOriginServerAndReadLine(passwordCmd);
		owner.echoLine(resp);
		if (!resp.toUpperCase().startsWith("+OK")) {
			return;
		}
		user.setPassword(passwordCmd.split(" ")[1]);
		getBundle().put("user", user);
		statsService.incrementNumberOfAccesses(user.getMail());
		owner.getStateMachine().setState(new ParseMailState(owner));
	}

	private String echoToOriginServerAndReadLine(String line) throws IOException {
		MailSocketService service = (MailSocketService) owner;
		service.echoLineToOriginServer(line);
		return service.readFromOriginServer().readLine();
	}
	
	private boolean validateAccessToMailByTime(User user) {
		MailSocketService mailServer = (MailSocketService) owner;
		String userMail = user.getMail();
		logger.debug("Checking time access for user: " + userMail);
		try {
			new TimeValidator(userMail).validate();
			return true;
		} catch (LoginValidationException e) {
			logger.info("User " + userMail + " is banned. Closing connection.");
			mailServer.echoLine("-ERR User does not have acces during this time.");
			return false;
		}
	}
	
	private boolean validateAccessToMailByCount(User user) {
		MailSocketService mailServer = (MailSocketService) owner;
		String userMail = user.getMail();
		logger.debug("Checking amount of accesses for user: " + userMail);
		try {
			new AccessCountValidator(userMail).validate();
			return true;
		} catch (LoginValidationException e) {
			logger.info("User " + userMail + " is banned. Closing connection.");
			mailServer.echoLine("-ERR No tiene acceso, ya entro demasiadas veces hoy.");
			return false;
		}
	}
}
