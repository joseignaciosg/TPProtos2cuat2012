package service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import model.User;
import model.configuration.Config;
import model.mail.MailRetriever;
import model.mail.MailTransformer;
import model.parser.mime.MailMimeParser;
import model.validator.LoginValidationException;
import model.validator.MailDeleteValidator;
import model.validator.UserLoginValidator;

import org.apache.log4j.Logger;

import service.state.impl.mail.AuthState;
import service.state.impl.mail.TransactionMailState;

public class MailSocketService extends AbstractSockectService {

	private static final Logger logger = Logger.getLogger(MailSocketService.class);

	private Socket originServerSocket;

	private MailRetriever mailRetriever;
	private MailTransformer mailTranformer;
	private MailMimeParser mailMimeParser;
	private MailDeleteValidator mailDeletionValidator;
	private UserLoginValidator userLoginvalidator;
	private DataOutputStream outToOriginServer;

	public MailSocketService(Socket socket) {
		super(socket);
		stateMachine.setState(new AuthState(this));
		mailRetriever = new MailRetriever();
		mailTranformer = new MailTransformer();
		mailMimeParser = new MailMimeParser();
		userLoginvalidator = new UserLoginValidator();
		mailDeletionValidator = new MailDeleteValidator();
	}

	@Override
	protected void onConnectionEstabished(boolean endOfTransmission) throws Exception {
		super.onConnectionEstabished(endOfTransmission);
		if(!endOfTransmission){
			echoLine("+OK Mail Proxy Ready.");
		}else{
			echoLine("-ERR Your ip is banned.");
		}
		
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
			outToOriginServer.close();
		}
		super.onConnectionClosed();
	}

	public boolean hasOriginServerSocket() {
		return originServerSocket != null;
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

	public MailMimeParser getMailMimeParser() {
		return mailMimeParser;
	}

	public UserLoginValidator getUserLoginvalidator() {
		return userLoginvalidator;
	}

	public MailDeleteValidator getMailDeletionValidator() {
		return mailDeletionValidator;
	}
	
	public void echoLineToOriginServer(String s) throws IOException {
		logger.trace("Echo to origin server: " + s);
		echoToOriginServer(s + "\r\n");
	}

	public void echoToOriginServer(String s) throws IOException {
		outToOriginServer.writeBytes(s);
	}

	public BufferedReader readFromOriginServer() throws IOException {
		return new BufferedReader(new InputStreamReader(
				originServerSocket.getInputStream()));
	}

	public String setOriginServer(String host) throws IOException {
		logger.info("Origin server is now: " + host);
		int port = Config.getInstance().getGeneralConfig().getInt("pop3_port");
		setOriginServerSocket(new Socket(host, port));
		String line = readFromOriginServer().readLine();
		logger.info("Mail server new connection status: " + line);
		return line;
	}

	public void setOriginServerSocket(Socket originServerSocket)
			throws IOException {
		this.originServerSocket = originServerSocket;
		outToOriginServer = new DataOutputStream(
				originServerSocket.getOutputStream());
	}

	public void userLoggedIn(User user) throws LoginValidationException {
		userLoginvalidator.validateUserLogin(user);
		getStateMachine().getBundle().put("user", user);
		getStateMachine().setState(new TransactionMailState(this));
		statsService.incrementNumberOfAccesses(user.getMail());
		logger.info(user.getMail() + " logueado correctamente.");
	}
}
