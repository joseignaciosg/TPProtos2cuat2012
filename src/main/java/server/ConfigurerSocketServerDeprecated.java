package server;

import java.io.DataOutputStream;

import util.Config;

public class ConfigurerSocketServerDeprecated extends AbstractSockectServer {

	private static Config configurerConfig = Config.getInstance().getConfig("configurer_conf");

	private DataOutputStream outToClient;
	private boolean loggedIn = false;
	private int maxInvalidLogInAttempts;

	@Override
	protected boolean exec(final String command) throws Exception {
		if (!this.loggedIn) {
			this.loggedIn = this.validatePassword(command);
			if (!this.loggedIn) {
				if (this.maxInvalidLogInAttempts++ == 3) {
					this.outToClient
							.writeBytes("Error: Reached maximum attemps of invalid logins. Closing connection...\n");
					return true;
				}
				this.outToClient.writeBytes("PASSWORD: ");
			} else {
				this.outToClient.writeBytes("file: ");
			}
			return false;
		} else {
			this.outToClient.writeBytes("Scanning file: " + command + "...\n");
			this.outToClient.writeBytes("file: ");

		}
		if ("QUIT".equals(command.toUpperCase())) {
			this.end();
			return true;
		}
		return false;
	}

	private boolean validatePassword(final String command) {
		if (configurerConfig.get("password").equals(command)) {
			return true;
		}
		return false;
	}

	@Override
	protected void initialize() throws Exception {
		this.outToClient = new DataOutputStream(this.socket.getOutputStream());
		this.maxInvalidLogInAttempts = 0;
		super.initialize();
		this.outToClient.writeBytes(this.getHeader());
		this.outToClient.writeBytes("PASSWORD: ");
	}

	@Override
	protected void end() throws Exception {
		this.loggedIn = false;
	}

	private String getHeader() {
		return "\n\n**********************************************************\n**\t\tPOP3 Proxy - Configurer Server\t\t**\n**********************************************************\n\n\n";
	}

}
