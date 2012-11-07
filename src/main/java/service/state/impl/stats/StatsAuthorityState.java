package service.state.impl.stats;

import service.AbstractSockectService;
import service.command.impl.ExitCommand;
import service.command.impl.stats.StatsAuthCommand;
import service.state.State;

public class StatsAuthorityState extends State {

	public StatsAuthorityState(AbstractSockectService owner) {
		super(owner);
		commandRecognizer.register("AUTH", StatsAuthCommand.class);
		commandRecognizer.register("EXIT", ExitCommand.class);
	}
	
}
