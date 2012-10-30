package service.command.impl.stats;

import service.AbstractSockectService;
import service.StatusCodes;
import service.command.ServiceCommand;
import service.state.impl.stats.StatsAuthorityState;

public class StatsLogOutCommand extends ServiceCommand {

	public StatsLogOutCommand(AbstractSockectService owner) {
		super(owner);
	}

	@Override
	public void execute(String[] params) {
		owner.getStateMachine().setState(new StatsAuthorityState(owner));
		owner.echoLine(StatusCodes.OK_LOGGED_OUT);
	}

}
