package server.command.impl;

import server.command.ServiceCommand;
import server.state.impl.AuthorityState;

public class LogOutCommand extends ServiceCommand {

	@Override
	public void execute(String[] params) {
		owner.getStateMachine().setState(new AuthorityState(owner));
		owner.echoLine(0, "Logged Out");
	}

}
