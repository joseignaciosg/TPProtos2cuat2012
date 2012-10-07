package mock;

import java.io.DataOutputStream;

import server.AbstractSockectServer;
import server.Server;


public class MockedSocketEmailServer extends AbstractSockectServer {
	
	private Server server;
	
	private DataOutputStream outToClient;
	
	public MockedSocketEmailServer() {
		server = new MockitoServer();
	}
	
	@Override
	protected void initialize() throws Exception {
		outToClient = new DataOutputStream(socket.getOutputStream());
	}
	
	@Override
	protected boolean exec(String command) throws Exception {
		String ans = server.exec(command);
		if (ans == null) {
			outToClient.writeBytes("I can break the rules too, good bye!\n");
			return true;
		}
		outToClient.writeBytes(ans);
		return "QUIT".equals(ans.toUpperCase());
	}


}
