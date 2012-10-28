package service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import model.util.StringUtil;

import org.apache.log4j.Logger;

import service.command.impl.stats.StatsService;
import service.state.ServiceStateMachine;

public abstract class AbstractSockectService implements Runnable {

	private static final Logger logger = Logger.getLogger(AbstractSockectService.class);
	
	protected static StatsService statsService = StatsService.getInstace();
	
	protected Socket socket;
	protected boolean endOfTransmission;
	protected ServiceStateMachine stateMachine;

	public AbstractSockectService(Socket socket) {
		this.socket = socket;
		stateMachine = new ServiceStateMachine(this);
		endOfTransmission = false;
	}
	
	@Override
	public void run() {
		try {
			onConnectionEstabished();
			while (!endOfTransmission) {
				BufferedReader inFromClient = read();
				String clientSentence = inFromClient.readLine(); 
				if (clientSentence != null) {
					exec(clientSentence);
				} else {
					// Connection has been closed or pipe broken...
					endOfTransmission = true;
				}
			}
		} catch (Exception e) {
			logger.error("Exception on run(). Closing Connection.");
			e.printStackTrace();
		}
		try {
			onConnectionClosed();
		} catch (Exception e) {
			logger.error("Could not close connection " + e.getMessage());
		}
	}

	protected void onConnectionEstabished() throws Exception {
	}
	
	protected abstract void exec(String command) throws Exception;
	
	protected void onConnectionClosed() throws Exception {
		stateMachine.exit();
		socket.close();
	}
	
	public void echoLine(StatusCodes statusCode) {
		echoLine(statusCode, null);
	}
	
	public void echoLine(StatusCodes statusCode, String data) {
		String msg = statusCode.message;
		if (!StringUtil.empty(data)) {
			msg += " (" + data + ")";
		}
		echoLine(statusCode.code, msg);
	}
	
	public void echoLine(int statusCode, String data) {
		if (statusCode < 100) {
			echoLine("+OK " + statusCode + " [ "  + data  + " ]");
		} else {
			echoLine("-ERR " + statusCode + " [ "  + data  + " ]");
		}
	}
	
	public void echoLine(String s) {
		logger.debug("Echo to client: " + s);
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
	
	public DataOutputStream getClientOutputStream(){
	    try {
	    	return new DataOutputStream(socket.getOutputStream());
	    } catch (IOException e) {
	    	logger.error("Could not write to output stream!. Reason: " + e.getMessage());
	    }
	    return null;
	}
	
	public BufferedReader read() {
		try {
			return new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			logger.error("Could not write to output stream!. Reason: " + e.getMessage());
			throw new IllegalStateException("Could not read from server!");
		}
	}
	
	public void setEndOfTransmission(boolean endOfTransmission) {
		this.endOfTransmission = endOfTransmission;
	}
	
	public ServiceStateMachine getStateMachine() {
		return stateMachine;
	}
	
	public Socket getSocket() {
		return socket;
	}

}
