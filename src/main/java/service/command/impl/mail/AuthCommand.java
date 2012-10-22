package service.command.impl.mail;

import java.io.IOException;

import model.User;
import model.util.CollectionUtil;

import org.apache.commons.net.util.Base64;
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
		String resp = echoToOriginServerAndReadLine(getOriginalLine());
		User user;
		if (CollectionUtil.empty(params)) {
			owner.echoLine(resp);
			return;
		} else if ("PLAIN".equals(params[0].toUpperCase())) {
			owner.echoLine(resp);
			String base64Credentials = owner.read().readLine();
			resp = echoToOriginServerAndReadLine(base64Credentials);
			owner.echoLine(resp);
			if (!resp.startsWith("+OK")) {
				return;
			}
			user = createUser(base64Credentials);
			getBundle().put("auth_user", user);
		} else if ("LOGIN".equals(params[0].toUpperCase())) {
			owner.echoLine(resp);
			String username = owner.read().readLine();
			resp = echoToOriginServerAndReadLine(username);
			owner.echoLine(resp);
			String password = owner.read().readLine();
			resp = echoToOriginServerAndReadLine(password);
			owner.echo(resp);
			if (!resp.startsWith("+OK")) {
				return;
			}
			user = createUser(username, password);
		} else {
			logger.error("Unknown login type.");
			owner.echoLine("-ERR");
			return;
		}
		getBundle().put("auth_user", user);
	}

	private String echoToOriginServerAndReadLine(String line) throws IOException {
		MailSocketService service = (MailSocketService) owner;
		service.echoLineToOriginServer(line);
		return service.readFromOriginServer().readLine();
	}

	private User createUser(String base64Credentials) {
		Object obj = new Base64().decode(base64Credentials.getBytes());
		String decoded = new String((byte[]) obj);
		String[] parts = decoded.split("\0");
		return new User(parts[1], parts[2]);
	}
	
	private User createUser(String base64Username, String base64Password) {
		String decodedUsername = new String((byte[]) new Base64().decode(base64Username.getBytes()));
		String decodedPasswd = new String((byte[]) new Base64().decode(base64Password.getBytes()));
		String username = decodedUsername.split("\0")[0];
		String passwd = decodedPasswd.split("\0")[0];
		return new User(username, passwd);
	}
}
