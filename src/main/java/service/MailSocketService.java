package service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import model.mail.MailRetriever;
import model.mail.MailTransformer;
import model.mail.transformerimpl.FileMailTransformer;
import model.util.Config;

import org.apache.log4j.Logger;

import service.state.impl.mail.AuthState;

public class MailSocketService extends AbstractSockectService {

	private static final Logger logger = Logger.getLogger(MailSocketService.class);
	
	private Socket originServerSocket;
	private MailRetriever mailRetriever;
	private MailTransformer mailTranformer;
	
	public MailSocketService(Socket socket) {
		super(socket);
		stateMachine.setState(new AuthState(this));
		mailRetriever = new MailRetriever();
		mailTranformer = new FileMailTransformer();
	}

	@Override
	protected void onConnectionEstabished() throws Exception {
		super.onConnectionEstabished();
		echoLine("+OK Mail Proxy Ready.");
	}
	
	@Override
	protected void exec(String command) throws Exception {
		logger.trace("Recieved: " + command);
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
	
	public MailTransformer getMailTranformer() {
		return mailTranformer;
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
	
	public String setOriginServer(String host) throws IOException {
		int port = Config.getInstance().getInt("pop3_port");
		setOriginServerSocket(new Socket(host, port));
		String line = readFromOriginServer().readLine();
		logger.debug("Mail server new connection status: " + line);
		return line;
	}
}
