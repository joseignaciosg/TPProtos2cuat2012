package service.state.impl.mail;

import model.validator.LoginValidationException;
import model.validator.loginvalidator.IpValidator;
import service.AbstractSockectService;
import service.command.impl.mail.AuthCommand;
import service.command.impl.mail.ProxyCapaCommand;
import service.command.impl.mail.QuitCommand;
import service.command.impl.mail.UnknownCommand;
import service.command.impl.mail.UnsupportedCommand;
import service.command.impl.mail.UserCommand;
import service.state.State;

public class AuthState extends State {

	public AuthState(AbstractSockectService owner) {
		super(owner);
		commandRecognizer.register("APOP", UnsupportedCommand.class);
		commandRecognizer.register("AUTH", AuthCommand.class);
		commandRecognizer.register("USER", UserCommand.class);
		commandRecognizer.register("QUIT", QuitCommand.class);
		// Non rfc stadard
		commandRecognizer.register("CAPA", ProxyCapaCommand.class);
		commandRecognizer.registerDefault(UnknownCommand.class);
	}

	@Override
	public void enter() {
		super.enter();
		String clientIp = owner.getSocket().getInetAddress().getHostAddress();
		logger.debug("Checking access for IP: " + clientIp);
		try {
			new IpValidator(clientIp).validate();
		} catch (LoginValidationException e) {
			logger.info("IP " + clientIp + " is banned. Closing connection.");
			owner.setEndOfTransmission(true);
		}
	}

}
