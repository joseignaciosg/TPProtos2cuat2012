package service;

import java.net.Socket;

import service.state.impl.configurer.ConfAuthorityState;

public class ConfigurerService extends AbstractSockectService {
	
	public ConfigurerService(Socket socket) {
		super(socket);
		stateMachine.setState(new ConfAuthorityState(this));
	}
	
	@Override
	protected void onConnectionEstabished() throws Exception {
		super.onConnectionEstabished();
		echoLine(StatusCodes.OK_CONFIGURER_READY);
	}
	
	@Override
	protected void exec(String command) throws Exception {
		stateMachine.exec(command.split(" "));
	}

}
