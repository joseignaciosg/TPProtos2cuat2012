package server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import parser.MimeParser;
import util.Config;


public class ProxySocketServer extends AbstractSockectServer {

	private Socket originServerSocket;
	private BufferedReader inFromOriginServer;
	private DataOutputStream outToMUA;
	private DataOutputStream outToOriginServer;
	private MimeParser mimeParser;

	@Override
	protected void initialize() throws Exception {
		mimeParser = new MimeParser();
		String originServerSentence;
		String address = Config.getInstance().get("mail_address");
		int port = Config.getInstance().getInt("mail_port");
		originServerSocket = new Socket(address, port);
		inFromOriginServer = new BufferedReader(new InputStreamReader(originServerSocket.getInputStream()));
		outToOriginServer = new DataOutputStream(originServerSocket.getOutputStream());
		originServerSentence = inFromOriginServer.readLine();
		System.out.println("PROXY: Received from Origin Server: " + originServerSentence);
		outToMUA = new DataOutputStream(socket.getOutputStream());
		outToMUA.writeBytes(originServerSentence + "\r\n");
	}

	@Override
	protected boolean exec(final String command) throws Exception {
		String serverResponse;
		outToOriginServer.writeBytes(command + "\r\n");
		if (command.equals("CAPA") || command.equals("LIST") || command.equals("UIDL")) {
			do {
				serverResponse = inFromOriginServer.readLine();
				outToMUA.writeBytes(serverResponse + "\r\n");
				System.out.println("PROXY: Received from Origin Server: " + serverResponse);
			} while (!serverResponse.equals("."));
		} else if (command.contains("RETR")) {
			mimeParser.parse(inFromOriginServer, outToMUA);
		} else {
			serverResponse = inFromOriginServer.readLine();
			outToMUA.writeBytes(serverResponse + "\r\n");
			System.out.println("PROXY: Received from Origin Server: " + serverResponse);
		}
		return false;
	}

}
