package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import util.Config;
import util.ConfigWriter;

public class ConfigurerSocketServer extends AbstractSockectServer {

	private static Logger logger = Logger.getLogger(ConfigurerSocketServer.class);
	private static Config configurerConfig = Config.getInstance().getConfig(
			"configurer_conf");

	private DataOutputStream outToClient;
	
	@Override
	public void run() {
		boolean endOfTransmission;
		try {
			this.initialize();
			do {
				final BufferedReader inFromClient = new BufferedReader(
						new InputStreamReader(this.socket.getInputStream()));
				final char[] clientSentence = new char[100];
				final int eof = inFromClient.read(clientSentence);
				final String clientSentenceString = new String(clientSentence);
				logger.info(this.getClass().getSimpleName() + " -- Command: " + clientSentenceString);
				if (eof != -1) {
					endOfTransmission = this.exec(clientSentenceString);
				} else {
					// Connection has been closed or pipe broken...
					endOfTransmission = true;
				}
			} while (!endOfTransmission);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		try {
			this.socket.close();
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void initialize() throws Exception {
		this.outToClient = new DataOutputStream(this.socket.getOutputStream());
		this.outToClient.writeBytes(this.getHeader());
	}
	
	@Override
	protected boolean exec(final String command) throws Exception {
		final String[] lines = command.split("\n");
		if (!this.validatePassword(lines[0])) {
			this.outToClient
					.writeBytes("The password is not correct. Try again\n.");
			return true;
		}
		for (int i = 0; i < lines.length; i++) {
			String fileName = lines[i];
			if (fileName.endsWith(".conf")) {
				ConfigWriter writer = new ConfigWriter(fileName);
				outToClient.writeBytes("configuring: " + lines[i++] + "\n");
				String line;
				do {
					line = lines[i++];
					if (!".".equals(line)) {
						writer.appendLine(line);						
						logger.debug("\tadding:" + line);
					}
				} while (!".".equals(line));
				writer.flush();
			}
		}
		return true;
	}

	private boolean validatePassword(final String command) {
		if (configurerConfig.get("password").equals(command)) {
			return true;
		}
		return false;
	}

	private String getHeader() {
		return "\n\n**********************************************************\n**\t\tPOP3 Proxy - Configurer Server\t\t**\n**********************************************************\n\n\n";
	}

}
