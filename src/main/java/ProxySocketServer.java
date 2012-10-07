import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

import server.AbstractSockectServer;

public class ProxySocketServer extends AbstractSockectServer {

	private Socket originServerSocket;
	private BufferedReader inFromOriginServer;
	private DataOutputStream outToMUA;
	private DataOutputStream outToOriginServer;

	@Override
	protected void initialize() throws Exception {
		String originServerSentence;
		//originServerSocket = new Socket("localhost", 8082);
		originServerSocket = new Socket("mail.josegalindo.com.ar", 110);
		inFromOriginServer = new BufferedReader(new InputStreamReader(originServerSocket.getInputStream()));
		outToOriginServer = new DataOutputStream(originServerSocket.getOutputStream());
		originServerSentence = inFromOriginServer.readLine();
		System.out.println("PROXY: Received from Origin Server: " + originServerSentence);
		outToMUA = new DataOutputStream(socket.getOutputStream());
		outToMUA.writeBytes(originServerSentence + "\r\n");
	}

	@Override
	protected boolean exec(String command) throws Exception {
		String serverResponse;
		outToOriginServer.writeBytes(command + "\r\n");
		if (command.equals("CAPA") || command.equals("LIST") || command.equals("UIDL") || command.contains("RETR")) {
			do {
				serverResponse = inFromOriginServer.readLine();
				outToMUA.writeBytes(serverResponse + "\r\n");
				System.out.println("PROXY: Received from Origin Server: " + serverResponse);
			} while (!serverResponse.equals("."));
		} else {
			serverResponse = inFromOriginServer.readLine();
			outToMUA.writeBytes(serverResponse + "\r\n");
			System.out.println("PROXY: Received from Origin Server: " + serverResponse);
		}
		return false;
	}

}
