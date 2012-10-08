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

			// Schedule the two timers to run with different delays.
			taskTimer.schedule(new MonitorTask("object1"), 0, timerDelay);
			
			InputStream in = getClass().getClassLoader().getResourceAsStream(Config.getInstance().get("statistics_file"));
			Scanner scanner = new Scanner(in);
			while(scanner.hasNextLine()){
				outToClient.writeBytes("\n\n" +  scanner.nextLine() + "\n\n" );
			}
		}
		
		if("QUIT".equals(command.toUpperCase())){
			end();
			return true;
		}
		
		return false;
	}

	private boolean validatePassword(String command) {
		if (Config.getInstance().get("monitor_password").equals(command)) {
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
		if(taskTimer != null){
			taskTimer.cancel();
		}
	}

	private String getHeader() {
		return Config.getInstance().get("monitor_header");
	}

}
