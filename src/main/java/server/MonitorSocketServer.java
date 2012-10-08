package server;

import java.io.DataOutputStream;
import java.util.Timer;

import monitor.MonitorTask;
import util.Config;

public class MonitorSocketServer extends AbstractSockectServer {

	private static Config monitorConfig = Config.getInstance().getConfig("monitor_conf");
	
	private DataOutputStream outToClient;
	private boolean loggedIn = false;
	private int maxInvalidLogInAttempts;
	private Timer taskTimer;

	@Override
	protected boolean exec(String command) throws Exception {
		if (!(loggedIn = validatePassword(command))) {
			if (maxInvalidLogInAttempts++ == 3) {
				outToClient.writeBytes("Error: Reached maximum attemps of invalid logins. Closing connection...\n");
				return true;
			}
			outToClient.writeBytes("PASSWORD: ");
			return false;
		} else {
			taskTimer = new Timer();
			long timerDelay = monitorConfig.getInt("refresh_rate_ms");                   // 5 seconds delay
			
			// Schedule the timer to run the task
			taskTimer.schedule(new MonitorTask("monitorTask", outToClient), 0, timerDelay);
		}
		if ("QUIT".equals(command.toUpperCase())) {
			end();
			return true;
		}
		return false;
	}

	private boolean validatePassword(String command) {
		if (monitorConfig.get("password").equals(command)) {
			loggedIn = true;
			return true;
		}
		return false;
	}

	@Override
	protected void initialize() throws Exception {
		outToClient = new DataOutputStream(socket.getOutputStream());
		maxInvalidLogInAttempts = 0;
		super.initialize();
		outToClient.writeBytes(getHeader());
		outToClient.writeBytes("PASSWORD: ");
	}
	
	@Override
	protected void end() throws Exception {
		taskTimer.cancel();
		loggedIn = false;
	}

	private String getHeader() {
		return "\n\n**********************************************************\n**\t\tPOP3 Proxy - Monitor Server\t\t**\n**********************************************************\n\n\n";
	}

}
