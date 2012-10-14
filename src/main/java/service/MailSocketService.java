package service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import parser.impl.MailRetriever;
import util.Config;

public class MailSocketService extends AbstractSockectService {

	private Socket originServerSocket;
	private BufferedReader inFromOriginServer;
	private DataOutputStream outToMUA;
	private DataOutputStream outToOriginServer;
	private MailRetriever mailWorker;
	
	public MailSocketService() {
		
	}

	@Override
	protected void onConnectionEstabished() throws Exception {
		
		this.mailWorker = new MailRetriever();
		String originServerSentence;
		final String address = Config.getInstance().get("mail_address");
		final int port = Config.getInstance().getInt("mail_port");
		this.originServerSocket = new Socket(address, port);
		this.inFromOriginServer = new BufferedReader(new InputStreamReader(
				this.originServerSocket.getInputStream()));
		this.outToOriginServer = new DataOutputStream(
				this.originServerSocket.getOutputStream());
		originServerSentence = this.inFromOriginServer.readLine();
		System.out.println("PROXY: Received from Origin Server: "
				+ originServerSentence);
		this.outToMUA = new DataOutputStream(this.socket.getOutputStream());
		this.outToMUA.writeBytes(originServerSentence + "\r\n");
	}

	@Override
	protected void exec(final String command) throws Exception {
		String serverResponse;
		this.outToOriginServer.writeBytes(command + "\r\n");
		if (command.equals("CAPA") || command.equals("LIST") || command.equals("UIDL")) {
			do {
				serverResponse = inFromOriginServer.readLine();
				outToMUA.writeBytes(serverResponse + "\r\n");
				System.out.println("PROXY: Received from Origin Server: " + serverResponse);
			} while (!serverResponse.equals("."));
		} else if (command.contains("RETR")) {
			mailWorker.retrieve(inFromOriginServer, outToMUA);
		} else {
			serverResponse = inFromOriginServer.readLine();
			outToMUA.writeBytes(serverResponse + "\r\n");
			System.out.println("PROXY: Received from Origin Server: " + serverResponse);
		}
		endOfTransmission = "QUIT".equals(command.toUpperCase());
	}

}
