package service;

import model.StatusCodes;
import service.state.impl.configurer.AuthorityState;

public class ConfigurerService extends AbstractSockectService {
	
	public ConfigurerService() {
		stateMachine.setState(new AuthorityState(this));
	}
	
	@Override
	protected void onConnectionEstabished() throws Exception {
		echoLine(StatusCodes.OK_CONFIGURER_READY);
	}
	
	@Override
	protected void exec(String command) throws Exception {
		stateMachine.exec(command.split(" "));
	}

}
