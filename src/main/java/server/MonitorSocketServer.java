package server;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.util.Scanner;
import java.util.Timer;

import monitor.MonitorTask;
import util.Config;

public class MonitorSocketServer extends AbstractSockectServer {

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
			long timerDelay = 2*1000;                   // 5 seconds delay
			InputStream in = getClass().getClassLoader().getResourceAsStream(Config.getInstance().get("statistics_file"));

			// Schedule the timer to run the task
			taskTimer.schedule(new MonitorTask("monitorTask", outToClient, in), 0, timerDelay);
			
			
		}
		
		if("QUIT".equals(command.toUpperCase())){
			finalizeTransmission();
			return true;
		}
		
		return false;
	}

	private boolean validatePassword(String command) {
		if (Config.getInstance().get("monitor_password").equals(command)) {
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
	protected void finalizeTransmission() throws Exception {
		if(taskTimer != null){
			taskTimer.cancel();
		}
	}

	private String getHeader() {
		return Config.getInstance().get("monitor_header");
	}

}
