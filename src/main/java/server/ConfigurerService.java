package server;

import server.state.AuthorityState;

public class ConfigurerService extends AbstractSockectService {
	
	public ConfigurerService() {
		stateMachine.setState(new AuthorityState(this));
	}
	
	@Override
	protected void onConnectionEstabished() throws Exception {
		echoLine("+OK 0 [Configurer ready]");
	}
	
	@Override
	protected void exec(String command) throws Exception {
		stateMachine.exec(command.split(" "));
	}
	
	@Override
	protected void onConnectionClosed() throws Exception {
		echoLine("+OK 0 [Connection closed!]");
	}

}
