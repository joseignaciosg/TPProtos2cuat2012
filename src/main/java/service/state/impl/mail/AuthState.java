package service.state.impl.mail;

import java.net.Socket;

import model.validator.IpValidator;
import service.AbstractSockectService;
import service.StatusCodes;
import service.command.impl.mail.AuthCommand;
import service.command.impl.mail.DefaultCommand;
import service.command.impl.mail.ProxyCapaCommand;
import service.command.impl.mail.QuitCommand;
import service.command.impl.mail.UserCommand;
import service.state.State;

public class AuthState extends State {

	public AuthState(AbstractSockectService owner) {
		super(owner);
		commandRecognizer.register("AUTH", AuthCommand.class);
		commandRecognizer.register("USER", UserCommand.class);
		commandRecognizer.register("CAPA", ProxyCapaCommand.class);
		commandRecognizer.register("QUIT", QuitCommand.class);
		commandRecognizer.registerDefault(DefaultCommand.class);
	}

	@Override
	public void enter() {
		super.enter();
		if (!validateAccessToMail()) {
			owner.setEndOfTransmission(true);
			return;
		}
	}

	private boolean validateAccessToMail() {
		IpValidator ipValidator = new IpValidator();
		Socket serviceSocket = owner.getSocket();
		String clientIp = serviceSocket.getInetAddress().getHostAddress();
		logger.debug("Checking access for IP: " + clientIp);
		boolean clientIPBanned = ipValidator.isBanned(clientIp);
		if (clientIPBanned) {
			logger.info("IP " + clientIp + " is banned. Closing connection.");
			owner.echoLine(StatusCodes.ERR_BANNED_IP, clientIp);
			return false;
		}
		return true;
	}

	@Override
	public void exec(String[] params) {
		commandRecognizer.exec(params);
	}

}
