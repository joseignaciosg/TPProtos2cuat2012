package service.command.impl.configurer;

import service.AbstractSockectService;
import service.StatusCodes;
import service.command.ServiceCommand;
import service.state.impl.configurer.ConfAuthorityState;

public class ConfLogOutCommand extends ServiceCommand {

	public ConfLogOutCommand(AbstractSockectService owner) {
		super(owner);
	}

	@Override
	public void execute(String[] params) {
		owner.getStateMachine().setState(new ConfAuthorityState(owner));
		owner.echoLine(StatusCodes.OK_LOGGED_OUT);
	}

}
