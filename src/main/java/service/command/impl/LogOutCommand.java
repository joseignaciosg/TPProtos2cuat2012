package service.command.impl;

import service.command.ServiceCommand;
import service.state.impl.configurer.AuthorityState;

public class LogOutCommand extends ServiceCommand {

	@Override
	public void execute(String[] params) {
		owner.getStateMachine().setState(new AuthorityState(owner));
		owner.echoLine(0, "Logged Out");
	}

}
