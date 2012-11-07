package service.command.impl.mail;

import java.io.IOException;

import model.User;
import model.util.CollectionUtil;
import model.validator.LoginValidationException;
import service.AbstractSockectService;
import service.MailSocketService;
import service.command.ServiceCommand;

public class UserCommand extends ServiceCommand {

	public UserCommand(AbstractSockectService owner) {
		super(owner);
	}

	@Override
	public void execute(String[] params) throws Exception {
		MailSocketService mailServer = (MailSocketService) owner;
		if (CollectionUtil.empty(params)) {
			mailServer.echo("-ERR Invalid parameters");
			return;
		}
		User user = new User(params[0], null);
		try {
			mailServer.getUserLoginvalidator().userCanLogin(user);
		} catch (LoginValidationException e) {
			mailServer.echoLine("-ERR " + e.getMessage());
			return;
		}
		mailServer.setOriginServer(user.getMailServer());
		String resp = echoToOriginServerAndReadLine(getOriginalLine());
		owner.echoLine(resp);
		if (!resp.toUpperCase().startsWith("+OK")) {
			return;
		}
		String passwordCmd = owner.read().readLine();
		resp = echoToOriginServerAndReadLine(passwordCmd);
		owner.echoLine(resp);
		if (passwordCmd.toLowerCase().equals("quit")) {
			owner.setEndOfTransmission(true);
			return;
		}
		if (!resp.toUpperCase().startsWith("+OK")) {
			return;
		}
		user.setPassword(passwordCmd.split(" ")[1]);
		mailServer.userLoggedIn(user);
	}

	private String echoToOriginServerAndReadLine(String line) throws IOException {
		MailSocketService service = (MailSocketService) owner;
		service.echoLineToOriginServer(line);
		return service.readFromOriginServer().readLine();
	}

}
