package service.command.impl.mail;

import java.io.IOException;

import model.User;
import model.configuration.Config;
import model.configuration.KeyValueConfiguration;
import model.util.CollectionUtil;

import org.apache.commons.net.util.Base64;
import org.apache.log4j.Logger;

import service.AbstractSockectService;
import service.MailSocketService;
import service.command.ServiceCommand;
import service.state.impl.mail.ParseMailState;

public class AuthCommand extends ServiceCommand {

	private static final Logger logger = Logger.getLogger(AuthCommand.class);

	public AuthCommand(AbstractSockectService owner) {
		super(owner);
	}

	@Override
	public void execute(String[] params) throws Exception {
		MailSocketService mailServer = (MailSocketService) owner;
		if (CollectionUtil.empty(params)) {
			mailServer.echoLine("+");
			mailServer.echoLine(".");
			return;
		} else if ("PLAIN".equals(params[0].toUpperCase())) {
			mailServer.echoLine("+");
			String base64Credentials = mailServer.read().readLine();
			User tmpUser = createUser(base64Credentials);
			String host = getMailServer(tmpUser);
			mailServer.setOriginServer(host);
			// login against REAL origin server
			String resp = echoToOriginServerAndReadLine(getOriginalLine());
			if (!"+".equals(resp.trim())) {
				return;
			}
			resp = echoToOriginServerAndReadLine(base64Credentials);
			mailServer.echoLine(resp);
			if (!resp.toUpperCase().startsWith("+OK")) {
				return;
			}
			getBundle().put("auth_user", tmpUser);
		} else if ("LOGIN".equals(params[0].toUpperCase())) {
			mailServer.echoLine("+ VXNlcm5hbWU6"); 	// Username:
			String base64Username = mailServer.read().readLine();
			mailServer.echoLine("+ UGFzc3dvcmQ6"); 	// Password:
			String base64Password = mailServer.read().readLine();
			User tmpUser = createUser(base64Username, base64Password);
			mailServer.setOriginServer(getMailServer(tmpUser));
			// login against REAL origin server
			echoToOriginServerAndReadLine(getOriginalLine());
			echoToOriginServerAndReadLine(base64Username);
			String result = echoToOriginServerAndReadLine(base64Password);
			mailServer.echoLine(result);
			if (!result.toUpperCase().startsWith("+OK")) {
				return;
			}
			getBundle().put("auth_user", tmpUser);
		} else {
			logger.error("Unknown login type.");
			owner.echoLine("-ERR");
			return;
		}
		owner.getStateMachine().setState(new ParseMailState(owner));
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
	
	private String getMailServer(User user) {
		KeyValueConfiguration originServerConfig = Config.getInstance().getKeyValueConfig("origin_server");
		String host = user.getMailhost();
		String server = originServerConfig.get(host);
		return server == null ? originServerConfig.get("default") : server;
	}
}
