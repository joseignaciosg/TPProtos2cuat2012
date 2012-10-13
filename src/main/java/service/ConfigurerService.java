package service;

import service.state.impl.AuthorityState;

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

}
