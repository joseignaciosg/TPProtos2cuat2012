package service.command.impl.mail;

import java.io.IOException;

import model.User;
import model.util.Base64Util;
import model.util.CollectionUtil;
import model.validator.LoginValidationException;

import org.apache.log4j.Logger;

import service.AbstractSockectService;
import service.MailSocketService;
import service.command.ServiceCommand;

public class AuthCommand extends ServiceCommand {

	private static final Logger logger = Logger.getLogger(AuthCommand.class);

	public AuthCommand(AbstractSockectService owner) {
		super(owner);
	}

	@Override
	public void execute(String[] params) throws Exception {
		MailSocketService mailServer = (MailSocketService) owner;
		User user;
		String okLine;
		if (CollectionUtil.empty(params)) {
			mailServer.echoLine("+");
			mailServer.echoLine(".");
			return;
		} else if ("PLAIN".equalsIgnoreCase(params[0])) {
			mailServer.echoLine("+");
			String base64Credentials = mailServer.read().readLine();
			user = createUser(base64Credentials);
			mailServer.setOriginServer(user.getMailServer());
			// login against REAL origin server
			String resp = echoToOriginServerAndReadLine(getOriginalLine());
			if (!"+".equals(resp.trim())) {
				mailServer.echoLine(resp);
				return;
			}
			okLine = echoToOriginServerAndReadLine(base64Credentials);
			if (!okLine.toUpperCase().startsWith("+OK")) {
				mailServer.echoLine(okLine);
				return;
			}
		} else if ("LOGIN".equals(params[0].toUpperCase())) {
			mailServer.echoLine("+ VXNlcm5hbWU6"); 	// Username:
			String base64Username = mailServer.read().readLine();
			mailServer.echoLine("+ UGFzc3dvcmQ6"); 	// Password:
			String base64Password = mailServer.read().readLine();
			user = createUser(base64Username, base64Password);
			mailServer.setOriginServer(user.getMailServer());
			// login against REAL origin server
			echoToOriginServerAndReadLine(getOriginalLine());
			echoToOriginServerAndReadLine(base64Username);
			okLine = echoToOriginServerAndReadLine(base64Password);
			if (!okLine.toUpperCase().startsWith("+OK")) {
				mailServer.echoLine(okLine);
				return;
			}
		} else {
			logger.error("Unknown login type: " + getOriginalLine());
			owner.echoLine("-ERR Unknown login type.");
			return;
		}
		try {
			mailServer.getUserLoginvalidator().userCanLogin(user);
			mailServer.userLoggedIn(user);
			mailServer.echoLine(okLine);
		} catch (LoginValidationException e) {
			mailServer.echoLine("-ERR " + e.getMessage());
			return;
		}
	}

	private String echoToOriginServerAndReadLine(String line) throws IOException {
		MailSocketService service = (MailSocketService) owner;
		service.echoLineToOriginServer(line);
		return service.readFromOriginServer().readLine();
	}

	private User createUser(String base64Credentials) {
		String decoded = Base64Util.decode(base64Credentials);
		String[] parts = decoded.split("\0");
		return new User(parts[1], parts[2]);
	}
	
	private User createUser(String base64Username, String base64Password) {
		String decodedUsername = Base64Util.decode(base64Username);
		String decodedPasswd = Base64Util.decode(base64Password);
		String username = decodedUsername.split("\0")[0];
		String passwd = decodedPasswd.split("\0")[0];
		return new User(username, passwd);
	}

}
