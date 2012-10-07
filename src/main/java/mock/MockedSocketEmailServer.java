package mock;

import server.AbstractSockectServer;
import server.Server;


public class MockedSocketEmailServer extends AbstractSockectServer {
	
	private Server server;
	private boolean forceExit;
	
	public MockedSocketEmailServer() {
		forceExit = false;
		server = new MockitoServer();
	}
	
	@Override
	protected String getWelcomeMessage() {
		return server.exec(null);
	}
	
	@Override
	protected String exec(String command) {
		String ans = server.exec(command);
		if (ans != null) {
			return ans;
		}
		forceExit = true;
		return "I can break the rules too, good bye!\n";
	}

	@Override
	protected boolean isEnd(String command) {
		return forceExit || command.toLowerCase().equals("quit");
	}


}
