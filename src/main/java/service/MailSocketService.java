package service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import org.apache.log4j.Logger;

import parser.impl.MailRetriever;
import service.state.impl.mail.ValidationState;

public class MailSocketService extends AbstractSockectService {

	private static final Logger logger = Logger.getLogger(MailSocketService.class);
	
	private Socket originServerSocket;
	private MailRetriever mailRetriever;
	
	public MailSocketService() {
		stateMachine.setState(new ValidationState(this));
	}

	@Override
	protected void onConnectionEstabished() throws Exception {
		stateMachine.exec(null);
		if (!endOfTransmission) {
			try {
				setOriginServerSocket(new Socket("mail.josegalindo.com.ar", 110));
				String line = readFromOriginServer().readLine();
				echoLine(line);
				logger.trace("PROXY: Received from Origin Server: " + line);
			} catch (Exception e) {
				logger.error("Error en estado de valdiacion del servidor");
			}
		}
	}

	@Override
	protected void exec(String command) throws Exception {
		stateMachine.exec(command.split(" "));
	}
	
	@Override
	protected void onConnectionClosed() throws Exception {
		if(originServerSocket != null){
			originServerSocket.close();
		}
		super.onConnectionClosed();
	}
	
	public void setOriginServerSocket(Socket mailServerSocket) {
		this.originServerSocket = mailServerSocket;
	}
	
	public Socket getOriginServerSocket() {
		return originServerSocket;
	}
	
	public MailRetriever getMailRetriever() {
		return mailRetriever;
	}

	public void echoLineToOriginServer(String s) {
		echoToOriginServer(s + "\r\n");
	}
	
	public void echoToOriginServer(String s) {
		try {
			DataOutputStream out = new DataOutputStream(originServerSocket.getOutputStream());
			out.writeBytes(s);
		} catch (IOException e) {
			logger.error("Could not write to output stream!. Reason: " + e.getMessage());
		}
	}
	
	public BufferedReader readFromOriginServer() {
		try {
			return new BufferedReader(new InputStreamReader(originServerSocket.getInputStream()));
		} catch (IOException e) {
			logger.error("Could not write to output stream!. Reason: " + e.getMessage());
			throw new IllegalStateException("Could not read from server!");
		}
	}
}
