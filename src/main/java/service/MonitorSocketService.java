package service;

import service.state.impl.monitor.AuthorityState;

public class MonitorSocketService extends AbstractSockectService {

	public MonitorSocketService() {
		stateMachine.setState(new AuthorityState(this));
	}
	
	@Override
	protected void onConnectionEstabished() throws Exception {
		echoLine(0, "Monitor ready");
	}
	
	@Override
	protected void exec(String command) throws Exception {
		stateMachine.exec(command.split(" "));
	}

}
