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
	private MimeParser mimeParser;

	@Override
	protected void initialize() throws Exception {
		this.mimeParser = new MimeParser();
		String originServerSentence;
		// originServerSocket = new Socket("localhost", 8082);
		this.originServerSocket = new Socket("mail.josegalindo.com.ar", 110);
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
	protected boolean exec(final String command) throws Exception {
		String serverResponse;
		this.outToOriginServer.writeBytes(command + "\r\n");
		if (command.equals("CAPA") || command.equals("LIST")
				|| command.equals("UIDL")) {
			do {
				serverResponse = this.inFromOriginServer.readLine();
				this.outToMUA.writeBytes(serverResponse + "\r\n");
				System.out.println("PROXY: Received from Origin Server: "
						+ serverResponse);
			} while (!serverResponse.equals("."));
		} else if (command.contains("RETR")) {
			this.mimeParser.parse(this.inFromOriginServer, this.outToMUA);
		} else {

			serverResponse = this.inFromOriginServer.readLine();
			this.outToMUA.writeBytes(serverResponse + "\r\n");
			System.out.println("PROXY: Received from Origin Server: "
					+ serverResponse);
		}
		return false;
	}

}
