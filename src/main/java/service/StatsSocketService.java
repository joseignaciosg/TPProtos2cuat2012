package service;

import java.net.Socket;

import service.state.impl.stats.StatsAuthorityState;
import service.state.impl.stats.StatsReadState;

public class StatsSocketService extends AbstractSockectService {

	public StatsSocketService(Socket socket) {
		super(socket);
		stateMachine.setState(new StatsAuthorityState(this));
	}
	
	@Override
	protected void onConnectionEstabished() throws Exception {
		super.onConnectionEstabished();
		echoLine(StatusCodes.OK_STATISTICS_READY);
	}
	
	@Override
	protected void exec(String command) throws Exception {
		stateMachine.exec(command.split(" "));
	}

	public void loggedIn() {
		getStateMachine().setState(new StatsReadState(this));
	}
}
