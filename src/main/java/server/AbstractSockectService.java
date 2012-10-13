package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.apache.log4j.Logger;

import server.state.ServiceStateMachine;

public abstract class AbstractSockectService implements Runnable {

	private static final Logger logger = Logger.getLogger(AbstractSockectService.class);
	
	protected Socket socket;
	protected boolean endOfTransmission;
	protected ServiceStateMachine stateMachine;

	public AbstractSockectService() {
		stateMachine = new ServiceStateMachine(this);
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {
		try {
			onConnectionEstabished();
			do {
				BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String clientSentence = inFromClient.readLine(); 
				System.out.println(getClass().getSimpleName() + " -- Command: " + clientSentence);
				if (clientSentence != null) {					
					exec(clientSentence);
				} else {
					// Connection has been closed or pipe broken...
					endOfTransmission = true;
				}
			} while (!endOfTransmission);
			onConnectionClosed();
		} catch (final Exception e) {
			e.printStackTrace();
		}
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void onConnectionEstabished() throws Exception {
	}
	
	protected abstract void exec(String command) throws Exception;
	
	protected void onConnectionClosed() throws Exception {
	}
	
	public void echoLine(int code, String message) {
		if (code == 0) {
			echoLine("+OK " + code + " [" + message + "]");
		} else {
			echoLine("-ERR " + code + " [" + message + "]");
		}
	}
	
	public void echoLine(String s) {
		echo(s + "\r\n");
	}
	
	public void echo(String s) {
		try {
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			out.writeBytes(s);
		} catch (IOException e) {
			logger.error("Could not write to output stream!. Reason: " + e.getMessage());
		}
	}
	
	public void setEndOfTransmission(boolean endOfTransmission) {
		this.endOfTransmission = endOfTransmission;
	}
	
	public ServiceStateMachine getStateMachine() {
		return stateMachine;
	}
}
