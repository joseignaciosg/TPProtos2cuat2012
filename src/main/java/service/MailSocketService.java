package service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import model.parser.impl.MailRetriever;
import model.util.Config;

import org.apache.log4j.Logger;

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
			int port = Config.getInstance().getInt("pop3_port");
			String addres = "mail.josegalindo.com.ar";
			// String addres = "localhost";
			setOriginServerSocket(new Socket(addres, port));
			String line = readFromOriginServer().readLine();
			echoLine(line);
		}
	}

	@Override
	protected void exec(String command) throws Exception {
		logger.trace("recieved: " + command);
		stateMachine.exec(command.split(" "));
	}
	
	@Override
	protected void onConnectionClosed() throws Exception {
		logger.trace("Connection closed");
		if (originServerSocket != null) {
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
		logger.trace("Echo to origin server: " + s);
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
