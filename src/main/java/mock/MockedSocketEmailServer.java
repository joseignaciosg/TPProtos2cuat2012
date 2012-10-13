package mock;

import java.io.DataOutputStream;

import server.AbstractSockectService;
import server.Server;


public class MockedSocketEmailServer extends AbstractSockectService {
	
	private Server server;
	
	public MockedSocketEmailServer() {
		server = new MockitoServer();
	}
		
	@Override
	protected void onConnectionEstabished() throws Exception {
		DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
		outToClient.writeBytes(server.exec(null));
	}
	
	@Override
	protected void exec(String command) throws Exception {
		DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
		String ans = server.exec(command);
		if (ans == null) {
			outToClient.writeBytes("I can break the rules too, good bye!\n");
			endOfTransmission = true;
			return;
		}
		outToClient.writeBytes(ans);
		endOfTransmission = "QUIT".equals(ans.toUpperCase());
	}
	

}
