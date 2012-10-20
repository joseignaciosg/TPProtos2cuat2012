package service;

import model.StatusCodes;
import service.state.impl.stats.AuthorityState;

public class StatsSocketService extends AbstractSockectService {

	public StatsSocketService() {
		stateMachine.setState(new AuthorityState(this));
	}
	
	@Override
	protected void onConnectionEstabished() throws Exception {
		echoLine(StatusCodes.OK_STATISTICS_READY);
	}
	
	@Override
	protected void exec(String command) throws Exception {
		stateMachine.exec(command.split(" "));
	}

}
