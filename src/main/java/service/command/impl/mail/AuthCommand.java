package service.command.impl.mail;

import java.io.IOException;

import model.User;

import org.apache.commons.net.util.Base64;
import org.apache.log4j.Logger;

import service.AbstractSockectService;
import service.MailSocketService;
import service.command.ServiceCommand;
import util.CollectionUtil;
public class AuthCommand extends ServiceCommand {

	private static final Logger logger = Logger.getLogger(AuthCommand.class);
	
	public AuthCommand(AbstractSockectService owner) {
		super(owner);
	}

	@Override
	public void execute(String[] params) {
		if (CollectionUtil.empty(params)) {
			System.out.println("Parametros vacios!!");
			return;
		}
		if ("PLAIN".equals(params[0].toUpperCase())) {
			String resp = echoAndgetResponse(getOriginalLine());		// +
			owner.echoLine(resp);
			String base64Credentials = readLineFromClient();
			resp = echoAndgetResponse(base64Credentials);
			owner.echoLine(resp);
			if (resp.startsWith("+OK")) {
				return;
			}
			User user = createUser(base64Credentials);
			getBundle().put("auth_user", user);
		} else {
			System.out.println("Unknown login type.");
		}
	}
	
	private String echoAndgetResponse(String line) {
		MailSocketService service = (MailSocketService) owner;
		service.echoLineToOriginServer(line);
		return readLineFromOriginServer();
	}
	
	private String readLineFromClient() {
		try {
			return owner.read().readLine();
		} catch (IOException e) {
			logger.error("Could not read line from client");
			throw new IllegalStateException();
		}
	}
	
	private String readLineFromOriginServer() {
		try {
			return ((MailSocketService) owner).readFromOriginServer().readLine();
		} catch (IOException e) {
			logger.error("Could not read line from origin server");
			throw new IllegalStateException();
		}
	}

	private User createUser(String base64Credentials) {
		Object obj = new Base64().decode(base64Credentials.getBytes());
		String decoded = new String((byte[]) obj);
		String[] parts = decoded.split("\0");
		return new User(parts[1], parts[2]);
	}
}
