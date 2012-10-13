package service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import util.Config;
import util.ConfigWriter;

public class _ConfigurerSocketServer extends AbstractSockectService {

	private static Logger logger = Logger.getLogger(_ConfigurerSocketServer.class);
	private static Config configurerConfig = Config.getInstance().getConfig("configurer_conf");

	private DataOutputStream outToClient;
	
	@Override
	public void run() {
		try {
			onConnectionEstabished();
			do {
				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				char[] clientSentence = new char[100];
				int eof = inFromClient.read(clientSentence);
				String clientSentenceString = new String(clientSentence);
				logger.info(getClass().getSimpleName() + " -- Command: " + clientSentenceString);
				if (eof != -1) {
					exec(clientSentenceString);
				} else {
					// Connection has been closed or pipe broken...
					endOfTransmission = true;
				}
			} while (!endOfTransmission);
		} catch (final Exception e) {
			e.printStackTrace();
		}
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onConnectionEstabished() throws Exception {
		outToClient = new DataOutputStream(socket.getOutputStream());
		outToClient.writeBytes(getHeader());
	}
	
	@Override
	protected void exec(String command) throws Exception {
		String[] lines = command.split("\n");
		if (!validatePassword(lines[0])) {
			outToClient.writeBytes("The password is not correct. Try again\n.");
			endOfTransmission = true;
			return;
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
		endOfTransmission = true;
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
