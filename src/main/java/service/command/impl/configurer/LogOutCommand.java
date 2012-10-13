package service.command.impl.configurer;

import service.AbstractSockectService;
import service.command.ServiceCommand;
import service.state.impl.configurer.AuthorityState;

public class LogOutCommand extends ServiceCommand {

	
	
	public LogOutCommand(AbstractSockectService owner) {
		super(owner);
	}

	@Override
	public void execute(String[] params) {
		owner.getStateMachine().setState(new AuthorityState(owner));
		owner.echoLine(0, "Logged Out");
	}

}
