package server;

import java.io.DataOutputStream;
import java.util.Timer;

import util.Config;
import worker.MonitorTask;

public class MonitorSocketServer extends AbstractSockectService {

	private static Config monitorConfig = Config.getInstance().getConfig("monitor_conf");
	
	private DataOutputStream outToClient;
	private boolean loggedIn = false;
	private int maxInvalidLogInAttempts;
	private Timer taskTimer;

	@Override
	protected void onConnectionEstabished() throws Exception {
		outToClient = new DataOutputStream(socket.getOutputStream());
		maxInvalidLogInAttempts = 0;
		outToClient.writeBytes(getHeader());
		outToClient.writeBytes("PASSWORD: ");
	}
	
	@Override
	protected void exec(String command) throws Exception {
		if (!loggedIn) {
			boolean validPass = validatePassword(command);
			if (!validPass) {
				maxInvalidLogInAttempts++;
				if (maxInvalidLogInAttempts == 3) {
					outToClient.writeBytes(
						"Error: Reached maximum attemps of invalid logins. Closing connection...\n");
					endOfTransmission = true;
					return;
				} else {
					outToClient.writeBytes("PASSWORD: ");
					return;
				}
			} else {
				taskTimer = new Timer();
				long timerDelay = monitorConfig.getInt("refresh_rate_ms");                   // 5 seconds delay
				taskTimer.schedule(new MonitorTask("monitorTask", outToClient), 0, timerDelay);
				loggedIn = true;
			}
			return;
		}
		endOfTransmission = "QUIT".equals(command.toUpperCase());
	}

	private boolean validatePassword(String command) {
		if (monitorConfig.get("password").equals(command)) {
			loggedIn = true;
			return true;
		}
		return false;
	}
	
	@Override
	protected void onConnectionClosed() throws Exception {
		taskTimer.cancel();
		loggedIn = false;
	}

	private String getHeader() {
		return "\n\n**********************************************************\n**\t\tPOP3 Proxy - Monitor Server\t\t**\n**********************************************************\n\n\n";
	}

}
